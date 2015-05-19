package cz.novros.dspace.rest.client.old;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.ProcessingException;

import org.dspace.rest.common.Bitstream;
import org.dspace.rest.common.Collection;
import org.dspace.rest.common.Item;
import org.dspace.rest.common.MetadataEntry;
import org.dspace.rest.common.ResourcePolicy;
import org.dspace.rest.common.ResourcePolicy.Action;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DSpaceRESTClientTest {

    /*
	private static Class<? extends DSpaceRESTClient> CLIENT_TYPE = PooledDSpaceRESTClient.class; // BasicDSpaceRESTClient.class
	private static DSpaceRESTClient client;
	private Item mockItem;
	private List<MetadataEntry> mockMetadata;
	private String collectionName = "Bakalářské práce - 11101";
	
	@BeforeClass
	public static void init() throws FileNotFoundException, IOException {
		String propertyFilePath = URLDecoder.decode(DSpaceRESTClientTest.class.getClassLoader().getResource("dspace-restapi.properties").getFile(),"UTF-8");
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertyFilePath));
		client = new DSpaceRESTClientFactoryBuilder().build(properties).getClient(CLIENT_TYPE);
	}
	
	@Before
	public void setup() {					
		mockMetadata = new ArrayList<MetadataEntry>();
        mockMetadata.add(new MetadataEntry("dc.contributor.advisor","Vedouci REST api",null));
        mockMetadata.add(new MetadataEntry("dc.contributor.author","REST api",null));
        mockMetadata.add(new MetadataEntry("dc.date.issued","2014-03-13",null));        
        mockMetadata.add(new MetadataEntry("dc.publisher","České vysoké učení technické v Praze. Vypočetní a informační centrum.","cze"));
        mockMetadata.add(new MetadataEntry("dc.rights","Vysokoškolská závěrečná práce je dílo chráněné autorským zákonem. Je možné pořizovat z něj na své náklady a pro svoji osobní potřebu výpisy, opisy a rozmnoženiny. Jeho využití musí být v souladu s autorským zákonem http://www.mkcr.cz/assets/autorske-pravo/01-3982006.pdf  a citační etikou http://knihovny.cvut.cz/vychova/vskp.html.","cze"));
        mockMetadata.add(new MetadataEntry("dc.rights","A university thesis is a work protected by the Copyright Act. Extracts, copies and transcripts of the thesis are allowed for personal use only and at one’s own expense. The use of thesis should be in compliance with the Copyright Act http://www.mkcr.cz/assets/autorske-pravo/01-3982006.pdf and the citation ethics http://knihovny.cvut.cz/vychova/vskp.html.","en"));
        mockMetadata.add(new MetadataEntry("dc.title","REST práce NG","cze"));
        mockMetadata.add(new MetadataEntry("dc.title.alternative","Toto je prace pres REST","cze"));
        mockMetadata.add(new MetadataEntry("dc.title","REST work NG","en"));
        mockMetadata.add(new MetadataEntry("dc.title.alternative","This is work over REST","en"));
        mockMetadata.add(new MetadataEntry("dc.type","Bakalářská práce","cze"));
        mockMetadata.add(new MetadataEntry("dc.date.accepted","2014-03-15",null));
        mockMetadata.add(new MetadataEntry("dc.contributor.referee","Oponent REST api",null));
        mockMetadata.add(new MetadataEntry("dc.language.iso","cze",null));
        mockMetadata.add(new MetadataEntry("dc.subject","hůlka, stonožka, matika, košík, náhoda, upír, zeď, polyp, boty, přechod","cze"));
        mockMetadata.add(new MetadataEntry("dc.subject","Wand, centipede, math, cart, chance, vampire, wall, polyp, shoes, transition","en"));
        mockMetadata.add(new MetadataEntry("dc.description.abstract","Google+ positive! Rychle jsem skočil ke dveřím, zamkl a zatáhl závěsy. Všichni pokradmu pokukovali k prázdnému místu u stolu, kde sedával. Generátor náhodných čísel Generátor náhodných čísel je zařízení nebo procedura, která generuje čísla která opravdu jsou nebo jen připomínají čísla náhodná. Až mě napadá, nebyl to záměr? Díky jeho odvaze ho přezdívali „bořitel měst“. Mirda nás nepustil k CB a to ani v neděli ráno vyhřátá místnost probudila i zazimovaného motýla Anténní systémy postaveny Jindrovo auto sloužilo jako kotvící kolík, no že je to konečně užitečné použití hihi a že to z Dvorské lítá hezky svědčí tento obrazek Ranní vysílání bylo pohodové, vyzobali jsme všechny opozdilce a kolem 11 začali uklízet.","cze"));
        mockMetadata.add(new MetadataEntry("dc.description.abstract","Google+ positive! I quickly jumped to the door, locked the door and drew the curtains. All sly glances to an empty seat at the table where he sat. Random Number Generator Random Number Generator is a device or procedure that generates numbers that really are or just remind random numbers. When I wonder, was it intentional? Thanks to his courage nicknamed him \"destroyer of cities\". Mirda kept us off the CB and even on Sunday morning warmed the room and woke zazimovaného butterfly antenna systems built JINDROVÁ auto served as an anchor pin, well it's finally useful hihi and use it from Dvorská flies nicely demonstrated this file Morning broadcast was relaxed, vyzobali we all stragglers and began to clean around 11.","en"));
        mockMetadata.add(new MetadataEntry("theses.degree.discipline","Informatika","cze"));
        mockMetadata.add(new MetadataEntry("theses.degree.grantor","Fakulta informačních technologií","cze"));
        mockMetadata.add(new MetadataEntry("theses.degree.name","Bc.","cze"));
        mockMetadata.add(new MetadataEntry("theses.degree.programme","Informační systémy a management","cze"));
        
        Collection parentCollection = client.findCollectionByName(collectionName);
		assertNotNull(parentCollection.getID());
		mockItem = client.createItem(parentCollection.getID(), mockMetadata);		
	}
	
	@Test
	public void testFindItemsById() {
		Item item = client.findItemById(mockItem.getID(), true, true);
		assertNotNull(item);
		Assert.assertNotNull(item.getID());
	}
	
	@Test
	public void testFindItemsByMetadataEntry() {
		List<Item> list = client.findItemsByMetadataEntry(new MetadataEntry("dc.contributor.author", "REST api", null));
		Assert.assertTrue(!list.isEmpty());
	}

	@Test
	public void testAddAndDeleteBitstream() throws ProcessingException, IOException {		
		Bitstream bitstream1 = new Bitstream();
		bitstream1.setName("pdf1.pdf");
		bitstream1.setDescription("Příliš žluťoučký kůň úpěl ďábelské ódy");		
		ResourcePolicy policy = new ResourcePolicy();
		policy.setGroupId(0);
		policy.setStartDate(new LocalDate().plusWeeks(1).toDate());		
		bitstream1.setPolicies(new ResourcePolicy[] {policy});
		
		bitstream1 = client.addBitstream(mockItem.getID(), bitstream1, new FileInputStream(getPath("pdf1.pdf")));		
		
		Bitstream bitstream2 = client.findBitstreamById(bitstream1.getID());		
		Assert.assertEquals(bitstream1.getID(), bitstream2.getID());
		Assert.assertEquals("pdf1.pdf", bitstream2.getName());
		Assert.assertEquals("Příliš žluťoučký kůň úpěl ďábelské ódy", bitstream2.getDescription());
		
		client.deleteBitstream(mockItem.getID(), bitstream1.getID());		
		Assert.assertNull(client.findBitstreamById(bitstream1.getID()));
	}
	
	@Test
	public void testModifyMetada() throws ProcessingException {
		mockMetadata = new ArrayList<MetadataEntry>();
        mockMetadata.add(new MetadataEntry("dc.title","Nový název","cze"));
        mockMetadata.add(new MetadataEntry("dc.title.alternative","New alternative title","cze"));
        mockMetadata.add(new MetadataEntry("dc.title","New title","en"));
        client.updateItem(mockItem.getID(), mockMetadata);
        
        Item item = client.findItemById(mockItem.getID(), true, true);
        
        for (MetadataEntry metadataEntry : item.getMetadata()) {
        	if ("dc.title".equals(metadataEntry.getKey()) && "cze".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("Nový název", metadataEntry.getValue());
        	} else if ("dc.title".equals(metadataEntry.getKey()) && "en".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("New title", metadataEntry.getValue());
        	} else if ("dc.title.alternative".equals(metadataEntry.getKey()) && "cze".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("New alternative title", metadataEntry.getValue());
        	} else if ("dc.type".equals(metadataEntry.getKey()) && "cze".equals(metadataEntry.getLanguage())) {
        		Assert.assertEquals("Bakalářská práce", metadataEntry.getValue());
        	}         	
        }        
	}
	
	@Test
	public void testModifyBitstream() throws ProcessingException, FileNotFoundException {
		Bitstream bitstream1 = new Bitstream();
		bitstream1.setName("pdf1.pdf");
		bitstream1.setDescription("desc1");		
		ResourcePolicy policy = new ResourcePolicy();
		policy.setGroupId(0);
		policy.setStartDate(new LocalDate().plusWeeks(1).toDate());		
		bitstream1.setPolicies(new ResourcePolicy[] {policy});
		
		bitstream1 = client.addBitstream(mockItem.getID(), bitstream1, new FileInputStream(getPath("pdf1.pdf")));		
		
		Bitstream bitstream2 = new Bitstream();
		bitstream2.setName("pdf2.pdf");
		bitstream2.setDescription("desc2");		
		policy = new ResourcePolicy();
		policy.setAction(Action.DELETE);
		policy.setGroupId(0);
		policy.setStartDate(new LocalDate().plusMonths(1).toDate());		
		bitstream2.setPolicies(new ResourcePolicy[] {policy});
		
		client.updateBitstream(bitstream1.getID(), bitstream2, new FileInputStream(getPath("pdf2.pdf")));		
		Bitstream bitstream3 = client.findBitstreamById(bitstream1.getID());
		
		Assert.assertEquals("pdf2.pdf", bitstream3.getName());
		Assert.assertEquals("desc2", bitstream3.getDescription());
	}
	
	@After
	public void tearDown() {
		client.deleteItem(mockItem.getID());
	}

	@AfterClass
	public static void shutdown() {
		((AbstractDSpaceRESTClient)client).destroy();
	}
	
	public static String getPath(String path) {
		try {
			return  URLDecoder.decode(DSpaceRESTClientTest.class.getClassLoader().getResource(path).getFile(),"UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return null;
	}
    */
}
