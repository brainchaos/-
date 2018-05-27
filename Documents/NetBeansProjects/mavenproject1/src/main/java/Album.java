import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Album {
    public static void main(String[] args) {
        try {
            
            String fileName = args[0];
            Integer avgYear = 0;
            Integer qty = 0;
            Integer k = 0;
            float maxPrice = 0;
            float price;
            List<Integer> maxPriceAlbumId = new ArrayList<>();
            String country;
            Map countryList = new HashMap();
            
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            //Document document = documentBuilder.parse("Album2.xml");
            Document document = documentBuilder.parse(fileName);
 
            // Получаем корневой элемент
            Node root = document.getDocumentElement();

            System.out.println("Детальная информация:\n");
            // Просматриваем все подэлементы корневого - т.е. альбомы
            NodeList albums = root.getChildNodes();
            for (int i = 0; i < albums.getLength(); i++) {
                Node album = albums.item(i);
                // Если нода не текст, то это альбом - заходим внутрь
                if (album.getNodeType() != Node.TEXT_NODE) {
                    NodeList albumProps = album.getChildNodes();
                    for(int j = 0; j < albumProps.getLength(); j++) {
                        Node albumProp = albumProps.item(j);
                        // Если нода не текст, то это один из параметров альбома - печатаем
                        if (albumProp.getNodeType() != Node.TEXT_NODE) {
                            System.out.println(albumProp.getNodeName() + ":" + albumProp.getChildNodes().item(0).getTextContent());
                            if ("PRICE".equals(albumProp.getNodeName())) {
                                price = Float.parseFloat(albumProp.getChildNodes().item(0).getTextContent());
                                if (maxPrice == price) {
                                    maxPriceAlbumId.add(k, i);
                                    k++;
                                }
                                if (maxPrice < price) {
                                    maxPrice = price;
                                    maxPriceAlbumId.clear();
                                    k = 0;
                                    maxPriceAlbumId.add(k, i);
                                    k++;
                                }
                            }
                            if ("COUNTRY".equals(albumProp.getNodeName())) {
                                country = albumProp.getChildNodes().item(0).getTextContent();
                                Integer quantity = (Integer) countryList.get(country);
                                countryList.put(country, quantity == null ? 1 : quantity + 1);
                            }
                            if ("YEAR".equals(albumProp.getNodeName())) {
                                avgYear += Integer.parseInt(albumProp.getChildNodes().item(0).getTextContent());
                                qty += 1;
                            }
                        }
                    }
                    System.out.println();
                }
            }
            avgYear = avgYear/qty;
            System.out.println("Самые дорогие альбомы: \n");

            for (int i = 0; i < maxPriceAlbumId.size(); i++) {
                k = maxPriceAlbumId.get(i);
               
                Node album = albums.item(k);
                if (album.getNodeType() != Node.TEXT_NODE) {
                    NodeList albumProps = album.getChildNodes();
                    for(int j = 0; j < albumProps.getLength(); j++) {
                        Node albumProp = albumProps.item(j);
                        if (albumProp.getNodeType() != Node.TEXT_NODE) {
                            System.out.println(albumProp.getNodeName() + ":" + albumProp.getChildNodes().item(0).getTextContent());
                        }
                    }
                }
                System.out.println();
            }
            
            System.out.println("Структура Map:\n");
            for (Iterator it = countryList.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                System.out.println("<Страна " + entry.getKey() + " > - "
                        + entry.getValue() + " раз");
            }
            System.out.println("\nCреднее значение года выпуска альбома : " + avgYear);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}