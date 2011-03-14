package au.org.ala.delta.rtf;

import java.util.HashMap;
import java.util.Map;

public abstract class Keyword {
	
	public static Map<String, Keyword> KEYWORDS = new HashMap<String, Keyword>();
	
	public static void registerKeyword(Keyword keywordDesc) {
		KEYWORDS.put(keywordDesc.getKeyword(), keywordDesc);
	}
	
	static {
		
		// Attribute keywords (keywords that alter character/section or document attributes)
		for (CharacterAttributeType attribute : CharacterAttributeType.values()) {
			registerKeyword(new AttributeKeyword(attribute.keyword(), attribute, 1, false));
		}
		
		// Character literal keywords...
		registerKeyword(new CharacterKeyword("\r", '\r'));
		registerKeyword(new CharacterKeyword("\n", '\n'));		
		
		// Destinations...
		registerKeyword(new DestinationKeyword("fonttbl", DestinationState.Header));	
		registerKeyword(new DestinationKeyword("colortbl", DestinationState.Header));
		registerKeyword(new DestinationKeyword("info", DestinationState.Header));
		registerKeyword(new DestinationKeyword("stylesheet", DestinationState.Header));
		
		// Special keywords
		registerKeyword(new UnicodeKeyword("u"));
	}

	protected String _keyword;
	protected KeywordType _type;

	public Keyword(String keyword, KeywordType type) {
		_keyword = keyword;
		_type = type;
	}

	public String getKeyword() {
		return _keyword;
	}
		
	public KeywordType getKeywordType() {
		return _type;
	}

	public static AttributeKeyword findAttributeKeyword(CharacterAttributeType attrType) {
		for (Keyword kwd : KEYWORDS.values()) {
			if (kwd instanceof AttributeKeyword) {
				AttributeKeyword attrKwd = (AttributeKeyword) kwd;
				if (attrKwd.getAttributeType() == attrType) {
					return attrKwd;
				}
			}
		}
		
		return null;
	}

}
