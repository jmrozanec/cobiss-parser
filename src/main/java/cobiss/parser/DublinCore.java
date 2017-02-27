package cobiss.parser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DublinCore {
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
    private String id;
    private String title;
    private String creator;
    private String subject;
    private String description;
    private String publisher;
    private String contributor;
    private DateTime date;
    private String type;
    private String format;
    private String identifier;
    private String source;
    private String language;
    private String relation;
    private String coverage;
    private String rights;

    //https://sl.wikipedia.org/wiki/Dublin_Core
    public DublinCore(String id, String title, String creator, String subject,
                      String description, String publisher, String contributor,
                      int date, String type, String format, String identifier,
                      String source, String language, String relation,
                      String coverage, String rights) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.subject = subject;
        this.description = description;
        this.publisher = publisher;
        this.contributor = contributor;
        this.date = toDate(""+date);
        this.type = type;
        this.format = format;
        this.identifier = identifier;
        this.source = source;
        this.language = language;
        this.relation = relation;
        this.coverage = coverage;
        this.rights = rights;
    }

    private DateTime toDate(String date){
        if(date.length()==4){
            return fmt.parseDateTime(String.format("%s0101", date));
        }
        if(date.length()==6){
            return fmt.parseDateTime(String.format("%s01", date));
        }
        if(date.length()==8){
            return fmt.parseDateTime(date);
        }
        return DateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getContributor() {
        return contributor;
    }

    public DateTime getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSource() {
        return source;
    }

    public String getLanguage() {
        return language;
    }

    public String getRelation() {
        return relation;
    }

    public String getCoverage() {
        return coverage;
    }

    public String getRights() {
        return rights;
    }
}
