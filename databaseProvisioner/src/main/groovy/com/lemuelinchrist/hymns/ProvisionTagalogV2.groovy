package com.lemuelinchrist.hymns

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * @author Lemuel Cantos
 * @since 1/7/2018
 */
class ProvisionTagalogV2 {

    static void main(String[] args) {
        for(int x=1; x<1399; x++) {
            try {
                println "finding hymn: " + x
                def element = new HymnElement(x)
                println element.getLyrics()
            } catch (HymnNotFoundException e) {
                println "not found: " + x
            }
        }

        for(int x=1; x<999; x++) {
            try {
                println "finding selected hymn: " + x
                def element = new HymnElement(x, "s")
                println element.getLyrics()
                println element.getSecondSetOfLyrics()
            } catch (HymnNotFoundException e) {
                println "not found: s" + x
            }
        }

    }

}

class HymnElement {
    public static String ROOT_PATH="tagalogV2/";

    Document doc
    int hymnNo
    String htmlId;
    Element baseElement
    String lyrics
    String type

    public HymnElement(int no, String prefix="") throws HymnNotFoundException {
        this.type=prefix.isEmpty()?"t":prefix
        this.doc=getDocumentFromHymn(no,prefix)
        htmlId="#" + type + String.format("%03d", no)
        baseElement=doc.select(htmlId)[0]
        if(baseElement==null) {
            throw new HymnNotFoundException()
        }


    }

    public String getLyrics() {
        getNextSiblingWithClass(baseElement.parent(),"hymnbody").select(".hymnbody p")[0].html()
    }
     public String getSecondSetOfLyrics() {
         try {
             def sibling = getNextSiblingWithClass(baseElement.parent(), "hymnbody")
             getNextSiblingWithClass(sibling, "hymnbody")
                     .select(".hymnbody p")[0].html()
         } catch (Exception e) {
             return null;
         }

     }

    private Element getNextSiblingWithClass(Element element, String className) {
        def siblingElement= element
        while(siblingElement.nextElementSibling().select("."+className)[0]==null) {
            siblingElement= siblingElement.nextElementSibling()
            if(siblingElement.select(".hymntitle")[0]!=null) throw new Exception("Lyrics not found!!!!");
        }
        siblingElement.nextElementSibling()
    }

    private Document getDocumentFromHymn(int no, String prefix="") {
        Jsoup.parse(getfileFromHymn(no, prefix),"UTF-8","")
    }

    private File getfileFromHymn(int no, String prefix="") {
        int hundreds = no / 100
        def path = ROOT_PATH + prefix + hundreds + "00-" + hundreds + "99" + ".html"
        println "getting file: " + path
        return new File(this.getClass().getClassLoader().getResource(path).getPath())
    }



}