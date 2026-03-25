package at.spengergasse.myproject.views.verwaltung;

import at.spengergasse.myproject.model.Notiz;
import at.spengergasse.myproject.model.NotizException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

@PageTitle("Verwaltung")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.FILE)
public class VerwaltungView extends VerticalLayout {

    private Grid<Notiz> grid;
    private TextField searchField;
    private Checkbox smallBigCheckbox;
    private HorizontalLayout searchStuff;
    private GridListDataView<Notiz> dataView;

    private Upload uploadCsv;
    private MemoryBuffer uploadBuffer;

    public VerwaltungView() {
        setSizeFull();

        initComponents();

        setDataView();

        addListeners();

        add(searchStuff, uploadCsv, grid);
    }

    private void addListeners() {
        //searchField.addValueChangeListener(e -> Notification.show("Suche"));
        searchField.addValueChangeListener(e -> dataView.refreshAll());
        smallBigCheckbox.addValueChangeListener(e -> dataView.refreshAll());

        uploadCsv.addSucceededListener(succeededEvent -> {
            //Notification.show(succeededEvent.getFileName());
            readCsvFile(uploadBuffer.getInputStream());
        });

        grid.addItemClickListener(item -> {
            Notification.show(item.getItem().toString());
        });
    }

    private void readCsvFile(InputStream inputStream) {
        String row;
        String[] elements;
        String description, catagory;
        LocalDate endDate;
        ArrayList<Notiz> notizen = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {

            row = bufferedReader.readLine();

            while (row != null) {
                //Arzttermin vereinbaren;Wichtig;2025-06-14
                elements = row.split(";");
                if(elements.length == 3){
                    description = elements[0];
                    catagory = elements[1];

                    try {
                        endDate = LocalDate.parse(elements[2]);
                        notizen.add(new Notiz(description, catagory, endDate));
                    } catch (NotizException e) {
                        Notification.show("readCsvFile: Kann Notiz nicht erstellen! " +
                                row + " " + e.getMessage());
                        System.out.println("readCsvFile: Kann Notiz nicht erstellen! " +
                                row + " " + e.getMessage());
                    } catch (DateTimeParseException e) {
                        Notification.show("readCsvFile: Falsches Datum Format! " +
                                row + " " + e.getMessage());
                        System.out.println("readCsvFile: Falsches Datum Format! " +
                                row + " " + e.getMessage());
                    }
                }
                else{
                    Notification.show("Zeile zu wenige Elemnte! " + row);
                    System.out.println("Zeile zu wenige Elemnte! " + row);
                }

                row = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Notification.show("readCsvFile: Kann Datei nicht lesen! " + e.getMessage());
        }

        grid.setItems(notizen);
        grid.recalculateColumnWidths();
        setDataView();
    }


    // Das Aussehen der Komponenten
    private void initComponents() {
        searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Suche");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setSuffixComponent(new Icon(VaadinIcon.AMBULANCE));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        smallBigCheckbox = new Checkbox("Groß- und Kleinschreibung beachten");
        smallBigCheckbox.setValue(true);

        searchStuff = new HorizontalLayout();
        searchStuff.setWidthFull();
        searchStuff.setVerticalComponentAlignment(Alignment.CENTER, smallBigCheckbox);
        searchStuff.add(searchField, smallBigCheckbox);

        uploadBuffer = new MemoryBuffer();
        uploadCsv = new Upload(uploadBuffer);

        initGrid();
    }

    private void setDataView() {
        //dataView = grid.setItems(generateTestData());

        dataView = grid.getListDataView();

        dataView.addFilter(this::filterNotiz);
        //dataView.addFilter(notiz -> filterNotiz(notiz));
    }

    private void initGrid() {
        grid = new Grid<>(Notiz.class, false);

        grid.addColumn(Notiz::remainingDays).setHeader("Tage").setAutoWidth(true).setSortable(true);
        grid.addColumn("description").setHeader("Aufgabe").setAutoWidth(true);
        grid.addColumn("category").setHeader("Kategorie").setAutoWidth(true);
        // grid.addColumn("endDate").setHeader("Bis-Datum").setAutoWidth(true);
        grid.addColumn(n -> {return n.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));})
                .setHeader("Enddatum").setAutoWidth(true).setSortable(true)
                .setComparator((n1, n2) -> n1.getEndDate().compareTo(n2.getEndDate()));

        grid.addComponentColumn(notiz -> {
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.getElement().setAttribute("theme", "error icon");

            deleteButton.addClickListener(buttonClickEvent -> {
//                Notification.show("Delete: " + notiz);
//                dataView.removeItem(notiz);
                deleteDialog(notiz);
            });


            return deleteButton;
        });

        grid.setItems(generateTestData());
        //grid.setItems(new ArrayList<>());
    }

    private void deleteDialog(Notiz notiz) {
        Notification.show("Öffnet den Delete Dialog für " + notiz);
        Dialog deleteDialog = new Dialog();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.add(new Span("Wollen Sie die Notiz wirklich löschen?"));

        Button confirmButton = new Button("Ja");
        confirmButton.getElement().setAttribute("theme", "error");
        confirmButton.addClassName("confirmButton");

        Button cancelButton = new Button("Nein");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(confirmButton, cancelButton);
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        verticalLayout.add(horizontalLayout);

        deleteDialog.add(verticalLayout);

        cancelButton.addClickListener(buttonClickEvent -> deleteDialog.close());
        confirmButton.addClickListener(buttonClickEvent -> {
            dataView.removeItem(notiz);
            deleteDialog.close();
        });

        deleteDialog.open();
    }

    private boolean filterNotiz(Notiz notiz){
        String searchString;
        boolean matchDescription;
        boolean matchCategory;
        if(smallBigCheckbox.getValue()){
            // Groß- Kleinschreibung wird beachtet
            searchString = searchField.getValue();
            matchDescription = notiz.getDescription().contains(searchString);
            matchCategory = notiz.getCategory().contains(searchString);
        }
        else{
            searchString = searchField.getValue().toLowerCase();
            matchDescription = notiz.getDescription().toLowerCase().contains(searchString);
            matchCategory = notiz.getCategory().toLowerCase().contains(searchString);
        }

        boolean matchEndDate = notiz.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .contains(searchString);
        boolean matchRemainingDays = (""+notiz.remainingDays()).contains(searchString);

        return matchDescription || matchCategory || matchEndDate || matchRemainingDays;
    }

    private ArrayList<Notiz> generateTestData(){
        ArrayList<Notiz> notizen = new ArrayList<>();
        try
        {
            notizen.add(new Notiz("Jogurt kaufen", "Einkaufen", LocalDate.now().plusDays(5)));
            notizen.add(new Notiz("Milch kaufen asdadsasdasddddddddddddddddddddd", "Einkaufen", LocalDate.now().plusDays(5)));
            notizen.add(new Notiz("Geschenk für Hugo", "Freunde", LocalDate.now().plusDays(15)));
            notizen.add(new Notiz("Java lernen", "Schule", LocalDate.now().plusDays(100)));
            notizen.add(new Notiz("Wocheneinkauf erledigen", "Alltag", LocalDate.now().plusDays(3)));
            notizen.add(new Notiz("Projektbericht fertigstellen", "Beruf", LocalDate.now().plusDays(2)));
            notizen.add(new Notiz("Geburtstagsgeschenk kaufen", "Familie", LocalDate.now().plusDays(7)));
            notizen.add(new Notiz("Fahrrad reparieren", "Hobby", LocalDate.now().plusDays(3)));
            notizen.add(new Notiz("Steuererklärung abgeben", "Finanzen", LocalDate.now().plusDays(5)));
            notizen.add(new Notiz("Reise nach Wien planen", "Reisen", LocalDate.now().plusDays(10)));
            notizen.add(new Notiz("Buch zu Ende lesen", "Freizeit", LocalDate.now().plusDays(7)));
            notizen.add(new Notiz("Arzttermin wahrnehmen", "Gesundheit", LocalDate.now().plusDays(1)));
            notizen.add(new Notiz("Lebenslauf aktualisieren", "Karriere", LocalDate.now().plusDays(5)));
            notizen.add(new Notiz("Wohnung putzen", "Alltag", LocalDate.now().plusDays(3)));
            notizen.add(new Notiz("Sport machen", "Fitness", LocalDate.now().plusDays(1)));
            notizen.add(new Notiz("Budget für den Monat erstellen", "Finanzen", LocalDate.now().plusDays(4)));
            notizen.add(new Notiz("Backrezept ausprobieren", "Hobby", LocalDate.now().plusDays(6)));
            notizen.add(new Notiz("Lebensmittelvorräte prüfen", "Alltag", LocalDate.now().plusDays(10)));
            notizen.add(new Notiz("Online-Kurs abschließen", "Bildung", LocalDate.now().plusDays(2)));
            notizen.add(new Notiz("Fotobuch gestalten", "Hobby", LocalDate.now().plusDays(6)));
            notizen.add(new Notiz("Spenden überweisen", "Soziales", LocalDate.now().plusDays(9)));
            notizen.add(new Notiz("Garten umgestalten", "Haus & Garten", LocalDate.now().plusDays(12)));
            notizen.add(new Notiz("Laptop aufräumen", "Technik", LocalDate.now().plusDays(8)));
            notizen.add(new Notiz("Steckbrief für Projektteam schreiben", "Beruf", LocalDate.now().plusDays(4)));
            notizen.add(new Notiz("Auto waschen", "Alltag", LocalDate.now().plusDays(5)));
            notizen.add(new Notiz("Freund zum Essen einladen", "Soziales", LocalDate.now().plusDays(9)));
            notizen.add(new Notiz("Wand streichen", "Haus & Garten", LocalDate.now().plusDays(12)));
            notizen.add(new Notiz("Altkleider spenden", "Soziales", LocalDate.now().plusDays(6)));
            notizen.add(new Notiz("Neuen Laptop recherchieren", "Technik", LocalDate.now().plusDays(8)));

        } catch (NotizException e)
        {
            System.out.println(e.getMessage());
        }

        return notizen;
    }

}
