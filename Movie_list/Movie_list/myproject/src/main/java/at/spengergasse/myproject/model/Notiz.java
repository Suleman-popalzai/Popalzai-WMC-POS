package at.spengergasse.myproject.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Notiz
{
    private String description;
    private String category;
    private LocalDate endDate;

    public Notiz(String description, String category, LocalDate endDate) throws NotizException
    {
        setDescription(description);
        setCategory(category);
        setEndDate(endDate);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description) throws NotizException
    {
        if (description == null || description.isEmpty())
            throw new NotizException("null Ref oder leere Notiz");
        this.description = description;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category) throws NotizException
    {
        if (description == null || description.isEmpty())
            throw new NotizException("null Ref oder leere Notiz");
        this.category = category;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) throws NotizException
    {
        if (endDate == null || endDate.isBefore(LocalDate.now()))
            throw new NotizException("null Ref oder Enddatum in Vergangenheit");
        this.endDate = endDate;
    }

    public long remainingDays()
    {
        return LocalDate.now().until(endDate, ChronoUnit.DAYS);
    }

    public String toCsvString()
    {
        return description + ";" + category + ";" + endDate;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Notiz: ");
        sb.append(description);
        sb.append(", ").append(category);
        sb.append(", Ende: ").append(endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        sb.append(", Tage: ").append(remainingDays());
        return sb.toString();
    }
}
