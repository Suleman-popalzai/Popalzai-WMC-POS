package at.spengergasse.myproject.views.add;

import at.spengergasse.myproject.model.Notiz;
import at.spengergasse.myproject.model.NotizException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

@PageTitle("Add")
@Route("empty")
@Menu(order = 1, icon = LineAwesomeIconUrl.FILE)
public class AddView extends VerticalLayout {

    // description, endDate, category
    private ComboBox<String> category;
    private TextArea description;
    private DatePicker endDate;
    private Button saveButton;
    private Button clearButton;
    private FormLayout formLayout;
    private HorizontalLayout buttonLayout;
    private Paragraph createResultParagraph;
    private ArrayList<Paragraph> createdNotizen;

    public AddView() {
        setSizeFull();
        initComponents();
        addListeners();

        add(formLayout, buttonLayout, createResultParagraph);
    }

    private void addListeners() {
        category.addValueChangeListener(event -> showFormFields());
        clearButton.addClickListener(event -> clearForm());
        saveButton.addClickListener(e -> createNotiz());
    }

    private void createNotiz() {
        String categoryValue = category.getValue();
        String descrptionValue = description.getValue();
        LocalDate endDateValue = endDate.getValue();

        try {
            Notiz notiz = new Notiz(descrptionValue, categoryValue, endDateValue);
            Paragraph paraNotiz = new Paragraph(notiz.toString());
            createdNotizen.add(paraNotiz);
            createResultParagraph.setText("Notiz wurde erstellt");
            createResultParagraph.getStyle().setColor("green");
            add(paraNotiz);
        } catch (NotizException e) {
            String letzterText = createResultParagraph.getText();
            createResultParagraph.setText(letzterText + "Notiz konnte nicht erstellt werden: " + e.getMessage());
            createResultParagraph.getStyle().setColor("red");
        }
    }

    private void clearForm() {
        category.clear();
        description.clear();
        endDate.setValue(LocalDate.now());

        createResultParagraph.setText("");

        for(Paragraph para : createdNotizen){
            remove(para);
        }
        createdNotizen.clear();
    }

    private void showFormFields() {
        String categoryValue = category.getValue();

        if(categoryValue == null || categoryValue.isEmpty()){
            description.setVisible(false);
            endDate.setVisible(false);
        }
        else {
            description.setVisible(true);
            endDate.setVisible(true);
        }
    }

    private void initComponents() {
        category = new ComboBox<>("Kategorie");
        category.setItems("Einkaufen", "Wichtig", "Arbeit", "Schule");
        category.setRequired(true);

        description = new TextArea("Beschreibung");
        description.setPlaceholder("Beschreiben Sie hier die Notiz...");
        description.setRequired(true);
        description.setMinHeight("150px");
        description.setVisible(false);

        endDate = new DatePicker("End Datum");
        endDate.setVisible(false);
        endDate.setValue(LocalDate.now());
        endDate.setLocale(Locale.GERMANY);

        formLayout = new FormLayout();
        formLayout.add(category, endDate, description);
        formLayout.setColspan(description, 2);

        saveButton  = new Button("Speichern");
        clearButton = new Button("Leere Formular");
        clearButton.getStyle().set("color", "red");
        buttonLayout = new HorizontalLayout();
        buttonLayout.add(saveButton, clearButton);

        createResultParagraph = new Paragraph();
        createResultParagraph.getStyle().setFontSize("2em");

        createdNotizen = new ArrayList<>();
    }

}
