import java.util.HashMap;
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Set;
//import java.util.TreeSet;

public class SpanishEnglishDictionary {
	
	public HashMap<String, String> dictionary() {
		
		/* Set of words:
		 * ------------------------------------------------------------------------------
		["Estas, Alicia, Aquel, Carroll, Cuando, De, Debido, Desde, En, Esta, 
		Estados, Gracias, Lewis, Limitar, Lo, Los, Siéntate, Ucrania., Unidos, 
		a, actualmente?`, adentro,, adonde, agente, al, algo, alimentos, almohada., 
		ambiente, ansias, apetecía, aprendiendo, aprendí, aquí?, arduamente, arrastrado, 
		astronómicos,, asunto, autógrafos, ayudar, bebidas, belleza, beneficios,, 
		blog., bosques, botellas, cada, caldeado, calle., calma, calorías, capital, 
		cardinales,, celebridad, champú, chocolate, ciclo, ciudades, ciudád, clave, 
		coches, colores,, comenzó, comerciante, como, compañía, con, conforme, confortable,, 
		conocer, correspondencia,, cruzadas., cuatro, cuatros, cuán, cálculos, da, de, 
		declaró, del, dentro, desapareció, descubrir, detestaba, difundir, dirigidas, días,, 
		días., e, ecológico, el, embargo,, en, entender,, entonces, equilibrio, erguida, es, 
		espalda, espíritu, estaba, estas, estuvieron, está, están, explosión, factor, familiar., 
		fiebre, fin, fronteras, futuro, genérico, gran, habitaciones, habitan., hablarme, había,
		hacer, haces, hasta, hija, historia, idea, imanes,, imponer,, importancia, importante, 
		información, ingesta, iniciativa, interesante, internet, la, las, le, llegarían, llevar, 
		los, lugar, manera, manos, mantener, maravillas, mariposas, mañana, me, memoria., minutes, 
		misma, monarca, muchas, mucho, mundo., más, nadie, natalidad,, ni, no, o, ofrezcan, otra, 
		otros, para, paró, pasa, pedían, película., pero, personas, peso, planificación, poco, 
		podemos, podía, polinizador, por, precio,, preocupaba, preocupan, primera,, problema, 
		programas, publicación, puntos, que, quince, residentes",, rápidas, sala, saludable., 
		se, sentirse, sentó, sido, similares, sin, social, sola, son, soportar,, su, sueños, 
		sufren,, sugerencias., sus, tecnologías, tiempo, tiempo,, trabajando, tranquila,, tranquilo,, 
		transmutación, términos, tú, un, una, uno:, usuario, van, velocidades, ver, verificada,, 
		vez, vida, vistosos, vital, vivía, y, ¿Qué, ¿Usaremos, éste, últimas, últimos, útiles]
		*/
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		// Named entities, not including in dict: Alicia, Carroll, Lewis, Estados, Ucrania, Unidos
		// phrases encountered: debido a, sin embargo
		
		map.put("estas", "these"); // feminine
		map.put("aquel", "that"); // that one over there; farther away
		map.put("cuando", "when");
		map.put("de", "of"); // or from, or other things dependent on context
		map.put("debido", "correctly"); // this is only when it's an adjective; usually a locative preposition "debido a", meaning "due to"
		map.put("desde", "since"); // or from if it's a preposition
		map.put("en", "in"); // or on, on top of, during, by
		map.put("esta", "this"); // feminine
		map.put("gracias", "thanks");
		map.put("limitar", "to limit"); // to restrict, to define
		map.put("lo", "it"); // or him, or "that which is" or "what is" if at the beginning of a sentence
		map.put("los", "the"); // or them
		map.put("siéntate", "sit down"); // this is an order
		map.put("a", "to"); // into, at; also used to indicate that an action is being done to a person
		map.put("actualmente", "at present"); // presently, currently, now, nowadays, these days
		map.put("adentro", "inside"); // inside of, indoors
		map.put("adonde", "where"); // wherever
		map.put("agente", "agent");
		map.put("al", "to the"); // masculine
		map.put("algo", "something");
		map.put("alimentos", "food"); // foodstuff, nourishment
		map.put("almohada", "pillow");
		map.put("ambiente", "environment"); // air, atmosphere; "medio ambiente" also means environment
		map.put("ansias", "desires"); // urges, impulses, desires, longings, wishes, depending on whether it's immediate or long-term
		map.put("apetecía", "fancied"); // craved, lusted for
		map.put("aprendiendo", "learning");
		map.put("aprendí", "I learned"); // no "I" if "yo" is already there
		map.put("aquí", "here");
		map.put("arduamente", "hard"); // this is an adverb
		map.put("arrastrado", "drawn"); // dragged, hauled, pulled, etc.
		map.put("astronómicos", "astronomical"); // referring to some plural, not exclusively feminine things
		map.put("asunto", "issue"); // matter, subject, problem, concern
		map.put("autógrafos", "autographs");
		map.put("ayudar", "to help");
		map.put("bebidas", "beverages");
		map.put("belleza", "beauty");
		map.put("beneficios", "benefits");
		map.put("blog", "blog");
		map.put("bosques", "forests"); // woods, woodlands
		map.put("botellas", "bottles");
		map.put("cada", "each");
		map.put("caldeado", "heated");
		map.put("calle", "street");
		map.put("calma", "calm"); // calmness, composure, unconcern
		map.put("calorías", "calories");
		map.put("capital", "capital"); // if adj; if noun, capital or principal, stock
		map.put("cardinales", "cardinal"); // adj modifying a plural noun
		map.put("celebridad", "celebrity");
		map.put("champú", "shampoo");
		map.put("chocolate", "chocolate");
		map.put("ciclo", "cycle");
		map.put("ciudades", "cities");
		map.put("ciudád", "city");
		map.put("clave", "key"); // code, cypher, clef, crypt
		map.put("coches", "cars");
		map.put("colores", "colors");
		map.put("comenzó", "began"); // started, commenced, launched; if not accompanied by pronoun/noun, refers to a he or a she
		map.put("comerciante", "merchant");
		map.put("como", "like"); // as
		map.put("compañía", "company");
		map.put("con", "with");
		map.put("conforme", "according"); // agreeable, conformant, agreed, acquiescent
		map.put("confortable", "comfortable"); // comfy, accomodative
		map.put("conocer", "to know"); // to recognize, to meet, to learn, to become acquainted
		map.put("correspondencia", "correspondence");
		map.put("cruzadas", "crossed"); // adjective modifying plural noun
		map.put("cuatro", "four");
		map.put("cuatros", "four"); // modifying plural noun
		map.put("cuán", "how");
		map.put("cálculos", "calculations"); // computations, estimations, calculus
		map.put("da", "gives"); // he/she gives
		map.put("declaró", "declared"); // he/she declared
		map.put("del", "of the"); // from the
		map.put("dentro", "within"); // inside, indoors
		map.put("desapareció", "disappeared"); // he/she disappeared
		map.put("descubrir", "to discover"); // to come upon, to expose, to find out
		map.put("detestaba", "detested"); // hated, loathed, abhorred
		map.put("difundir", "to spread"); // to diffuse, to transmit
		map.put("dirigidas", "directed"); // addressed, guided, leaded; adj modifying plural noun
		map.put("días", "days");
		map.put("e", "and");
		map.put("ecológico", "ecological");
		map.put("el", "the");
		map.put("embargo", "embargo"); // sin embargo means "however", this rarely pops up not preceded by "sin"
		map.put("entender", "understand");
		map.put("entonces", "then");
		map.put("equilibrio", "balance");
		map.put("erguida", "upright");
		map.put("es", "is"); // it is, this is, he is, she is...
		map.put("espalda", "back");
		map.put("espíritu", "spirit");
		map.put("estaba", "was");
		map.put("estuvieron", "were");
		map.put("está", "is");
		map.put("están", "are");
		map.put("explosión", "explosion");
		map.put("factor", "factor");
		map.put("familiar", "family"); // also familiar
		map.put("fiebre", "fever");
		map.put("fin", "end");
		map.put("fronteras", "borders");
		map.put("futuro", "future");
		map.put("genérico", "generic");
		map.put("gran", "great"); // large, big
		map.put("habitaciones", "lodgings"); // habitations, rooms, accomodations
		map.put("habitan", "inhabit"); // they inhabit
		map.put("hablarme", "to talk to me");
		map.put("había", "had");
		map.put("hacer", "to do"); // to make, to cause, etc.
		map.put("haces", "you do");
		map.put("hasta", "until"); // to, till, up, as far as, unto, through, even
		map.put("hija", "daughter");
		map.put("historia", "history"); // story, tale, record, past
		map.put("idea", "idea");
		map.put("imanes", "magnets");
		map.put("imponer", "to impose");
		map.put("importancia", "importance"); // value, magnitude	
		map.put("importante", "important");
		map.put("información", "information");
		map.put("ingesta", "intake");
		map.put("iniciativa", "initiative");
		map.put("interesante", "interesting");
		map.put("internet", "internet");
		map.put("la", "the"); // her, it; feminine
		map.put("las", "the"); // them; plural feminine
		map.put("le", "him"); // just as easily could be her or you
		map.put("llegarían", "would arrive"); // would get, would reach, would catch
		map.put("llevar", "to carry"); // lead, transport
		map.put("los", "the"); // masculine plural
		map.put("lugar", "place");
		map.put("manera", "way"); // manner, fashion
		map.put("manos", "hands");
		map.put("mantener", "to maintain"); // to sustain, to support
		map.put("maravillas", "wonders");
		map.put("mariposas", "butterflies");
		map.put("mañana", "morning"); // morning if preceded by "la", otherwise tomorrow
		map.put("me", "me");
		map.put("memoria", "memory");
		map.put("minutos", "minutes");
		map.put("misma", "same"); // feminine
		map.put("monarca", "monarch");
		map.put("muchas", "many"); // plural feminine
		map.put("mucho", "much");
		map.put("mundo", "world");
		map.put("más", "more");
		map.put("nadie", "no one"); // but sometimes can mean anyone
		map.put("natalidad", "birthrate");
		map.put("ni", "nor");
		map.put("no", "not"); // or no
		map.put("o", "or");
		map.put("ofrezcan", "offer");
		map.put("otra", "other"); // feminine
		map.put("otros", "others"); // masculine plural; if an adj, should be "other"
		map.put("para", "for"); // towards, to
		map.put("paró", "stopped");
		map.put("pasa", "pass"); // passes
		map.put("pedían", "requested");
		map.put("película", "movie");
		map.put("pero", "but");
		map.put("personas", "people");
		map.put("peso", "weight");
		map.put("planificación", "planning");
		map.put("poco", "little");
		map.put("podemos", "we can"); // or just can if we is already there, or we are able to 
		map.put("podía", "could");
		map.put("polinizador", "pollinator");
		map.put("por", "for"); // by, per, to, through
		map.put("precio", "price");
		map.put("preocupaba", "concerned"); // worried
		map.put("preocupan", "care"); // worry, trouble, bother
		map.put("primera", "first");
		map.put("problema", "problem");
		map.put("programas", "programs");
		map.put("publicación", "publication");
		map.put("puntos", "points");
		map.put("que", "that"); // what
		map.put("quince", "fifteen");
		map.put("residentes", "residents");
		map.put("rápidas", "fast"); // adj describing plural
		map.put("sala", "room");
		map.put("saludable", "healthy");
		map.put("se", "oneself"); //reflexive, usually can just get deleted
		map.put("sentirse", "to feel");
		map.put("sentó", "sat");
		map.put("sido", "been"); // was
		map.put("similares", "similar"); // adj describing plural
		map.put("sin", "without");
		map.put("social", "social");
		map.put("sola", "alone"); // single, only
		map.put("son", "are");
		map.put("soportar", "to support"); // to bear
		map.put("su", "his"); // her, their, its, your
		map.put("sueños", "dreams");
		map.put("sufren", "suffer"); // they suffer
		map.put("sugerencias", "suggestions");
		map.put("sus", "their"); // its, your
		map.put("tecnologías", "technologies");
		map.put("tiempo", "time"); // weather
		map.put("trabajando", "working");
		map.put("tranquila", "quiet"); // still, smooth, calm, serene, peaceful, tranquil; feminine
		map.put("tranquilo", "quiet"); // still, smooth, calm, serene, peaceful, tranquil; masculine
		map.put("transmutación", "transmutation");
		map.put("términos", "terms");
		map.put("tú", "you");
		map.put("un", "a"); // an
		map.put("una", "a"); // an
		map.put("uno", "one");
		map.put("usuario", "user");
		map.put("van", "go");
		map.put("velocidades", "speeds");
		map.put("ver", "to see");
		map.put("verificada", "verified");
		map.put("vez", "time"); // turn, occasion
		map.put("vida", "life");
		map.put("vistosos", "showy");
		map.put("vital", "vital");
		map.put("vivía", "lived");
		map.put("y", "and");
		map.put("qué", "what");
		map.put("usaremos", "we will use");
		map.put("éste", "this");
		map.put("últimas", "last"); // latest, most recent; adj or noun; feminine plural
		map.put("últimos", "last"); // latest, most recent; adj or noun; masculine plural
		map.put("útiles", "tools");
		map.put("ucrania", "Ukraine");
		map.put("estados", "States");
		map.put("unidos", "United");
		map.put("alicia", "Alice");
		return map;
	}
}
