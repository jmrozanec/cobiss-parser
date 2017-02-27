package cobiss.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CobissParser {
    public void parse(List<String> comarc){
        List<DublinCore> dublinCore = Lists.newArrayList();
        DublinCore dc = null;
        String state = "START";

        String id = "";
        String title = "";//2251: a f  (v)
        List<String> author = Lists.newArrayList();//700, 70111, 70201, 70211: b a
        String description = "";//2251: a f i (v)
        String publisher = "";//210: g
        String subject = "";//6100, 6101: z, a (z indicates lang used for tags, we should check is slv, and use [a] tags
        String date = ""; //210: d
        String type = "";//001: b, c
        String format = "";//215: a, d
        String identifier = ""; //COBISSID
        String language = ""; //100: h

        for(String line : comarc){
            if(line.contains("ID=")){
                if(dc!=null){
                    dublinCore.add(dc);
                }
                dc = new DublinCore(id, title, String.join(",", author), subject, description, publisher, "contributor",
                        date.isEmpty()?19910625:Integer.parseInt(date), type, format, identifier, "source",
                        language, "relation", "coverage", "rights");
                id = extractId(line);
            }
            String newstate = line.split(" ")[0];
            List<String>[] inputline = parseLine(line.replaceAll(newstate, ""));

            //title, author, subject, description, publisher, contributor, date, type, format identifier, source, language, relation, coverage, rights
            switch (newstate){
                case "2000":
                    title = buildTitle(inputline);
                    break;
                case "700":
                case "70111":
                case "70201":
                case "70211":
                    author.add(buildAuthor(inputline));
                    break;
                case "2251":
                    description = buildDescription(inputline);
                    break;
                case "210":
                    publisher = buildPublisher(inputline);
                    date = buildDate(inputline).split("-")[0];
                    break;
                case "6100":
                case "6101":
                    subject = buildSubject(inputline);
                    break;
                case "001":
                    type = buildType(inputline);
                    break;
                case "215":
                    format = buildFormat(inputline);
                    break;
                case "100":
                    language = buildLanguage(inputline);
                    break;
            }
        }
        dc = new DublinCore(id, title, String.join(",", author), subject, description, publisher, "contributor",
                date.isEmpty()?19910625:Integer.parseInt(date), type, format, identifier, "source", language, "relation", "coverage", "rights");
        dublinCore.add(dc);//TODO fill last element

        for(DublinCore core : dublinCore){
            System.out.println(">"+core.getTitle());
        }
    }

    private String extractId(String line){
        for(String item : line.split(" ")){
            if(item.startsWith("ID=")){
                return item.replace("ID=", "");
            }
        }
        return "";
    }

    private String buildTitle(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        Set<String> acceptable = Sets.newHashSet("a", "b", "e");
        Set<String> set = Sets.newHashSet();
        StringBuilder builder = new StringBuilder();
        for(int j=0; j<prefixes.size(); j++){
            if(acceptable.contains(prefixes.get(j)) && !set.contains(prefixes.get(j))){
                set.add(prefixes.get(j));
                String subtext = String.format(" %s", subtexts.get(j));
                builder.append(subtext);
            }
        }
        return builder.toString();
    }

    private String buildAuthor(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        String a = "";
        String b = "";
        List<String>names = Lists.newArrayList();
        for(int j=0; j<prefixes.size(); j++){
            if(prefixes.get(j).equals("a")){
                a = subtexts.get(j).trim();
            }
            if(prefixes.get(j).equals("b")){
                b = subtexts.get(j).trim();
            }
            if(!a.isEmpty()&&!b.isEmpty()){
                names.add(String.format("%s %s", b, a));
                a = "";
                b = "";
            }
        }
        return String.join(",", names);
    }

    private String buildDescription(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        Set<String> acceptable = Sets.newHashSet("a", "f", "i", "v");
        Set<String> set = Sets.newHashSet();
        StringBuilder builder = new StringBuilder();
        for(int j=0; j<prefixes.size(); j++){
            if(acceptable.contains(prefixes.get(j)) && !set.contains(prefixes.get(j))){
                set.add(prefixes.get(j));
                String subtext = String.format(" %s", subtexts.get(j));
                if(prefixes.get(j).equals("v")){
                    subtext = String.format(" (%s)", subtexts.get(j));
                }
                builder.append(subtext);
            }
        }
        return builder.toString();
    }

    private String buildPublisher(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        Set<String> acceptable = Sets.newHashSet("g");
        for(int j=0; j<prefixes.size(); j++){
            if(acceptable.contains(prefixes.get(j))){
                return subtexts.get(j);
            }
        }
        return "";
    }

    private String buildSubject(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        List<String> out = Lists.newArrayList();

        if(prefixes.indexOf("z")>-1 && subtexts.get(prefixes.indexOf("z")).startsWith("slv")){
            Set<String> acceptable = Sets.newHashSet("a");
            for(int j=0; j<prefixes.size(); j++){
                if(acceptable.contains(prefixes.get(j))){
                    out.add(subtexts.get(j));
                }
            }
        }
        return String.join(",", out);
    }

    private String buildDate(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        Set<String> acceptable = Sets.newHashSet("d");
        for(int j=0; j<prefixes.size(); j++){
            if(acceptable.contains(prefixes.get(j))){
                return subtexts.get(j);
            }
        }
        return "";
    }

    private String buildType(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        StringBuilder builder = new StringBuilder();
        Set<String> acceptable = Sets.newHashSet("b", "c");
        for(int j=0; j<prefixes.size(); j++){
            if(acceptable.contains(prefixes.get(j))){
                builder.append(String.format("%s ", subtexts.get(j)));
            }
        }
        return builder.toString().trim();
    }

    private String buildFormat(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        StringBuilder builder = new StringBuilder();
        Set<String> acceptable = Sets.newHashSet("a", "d");
        for(int j=0; j<prefixes.size(); j++){
            System.out.println("======");
            if(acceptable.contains(prefixes.get(j))){
                try{
                    builder.append(String.format("%s ", subtexts.get(j)));
                }catch (RuntimeException e){
                    System.out.println(Arrays.toString(prefixes.toArray()));
                    System.out.println(String.join("=", subtexts));
                    e.printStackTrace();
                }
            }
        }
        return builder.toString().trim();
    }

    private String buildLanguage(List<String>[] parsed){
        List<String> prefixes = parsed[0];
        List<String> subtexts = parsed[1];
        Set<String> acceptable = Sets.newHashSet("h");
        for(int j=0; j<prefixes.size(); j++){
            if(acceptable.contains(prefixes.get(j))){
                return subtexts.get(j);
            }
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private List<String>[] parseLine(String line) {
        List<String> prefix = Lists.newArrayList();
        List<String> subtexts = Lists.newArrayList();
        Pattern MY_PATTERN = Pattern.compile("\\[(.*?)\\]");
        Collections.addAll(subtexts, line.split("\\[[a-zA-Z0-9]+\\]"));
        subtexts = subtexts.stream().map(String::trim).filter(st->!st.isEmpty()).collect(Collectors.toList());
        Matcher m = MY_PATTERN.matcher(line);
        while (m.find()) {
            prefix.add(m.group(1));
        }
        return new List[]{prefix, subtexts};
    }
}