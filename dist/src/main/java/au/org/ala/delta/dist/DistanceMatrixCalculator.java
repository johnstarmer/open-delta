package au.org.ala.delta.dist;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.DeltaDataSet;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.NumericAttribute;
import au.org.ala.delta.model.NumericRange;

/**
 * Does the work of calculating the distance matrix that is the main output
 * of the DIST program.
 */
public class DistanceMatrixCalculator {

	private DistContext _context;
	private DeltaDataSet _dataSet;
	private List<Float> _ranges;
	private MultiStateDifferenceCalculator _multistateDifferenceCalculator;
	
	public DistanceMatrixCalculator(DistContext context) {
		_context = context;
		_dataSet = _context.getDataSet();
		initialiseRanges();
		if (context.getMatchOverlap()) {
			_multistateDifferenceCalculator = new MatchOverlapMultiStateDifferenceCalculator();
		}
		else {
			_multistateDifferenceCalculator = new DefaultMultiStateDifferenceCalculator();
		}
	}
	
	private void initialiseRanges() {
		_ranges = new ArrayList<Float>();
		
		for (int i=1; i<=_dataSet.getNumberOfCharacters(); i++) {
			if (_context.isCharacterExcluded(i)) {
				_ranges.add(-1f);
				continue;
			}
			Character character = _dataSet.getCharacter(i);
			float range = -1;
			if (character.getCharacterType().isNumeric()) {
				float min = Float.MAX_VALUE;
				float max = Float.MIN_VALUE;
				for (int j=1; j<=_dataSet.getMaximumNumberOfItems();j++) {
					Item item1 = _dataSet.getItem(j);
					NumericAttribute attribute = (NumericAttribute)item1.getAttribute(character);
					if (!attribute.isUnknown()) {
						float value = getSingleValue(attribute);
						if (value > -999f) {
							min = Math.min(min, value);
							max = Math.max(max, value);
						}
					}
				}
				range = Math.abs(max-min);
			}
			
			_ranges.add(range);
		}
		
	}
	
	public DistanceMatrix calculateDistanceMatrix() {
		DistanceMatrix matrix = new DistanceMatrix(_dataSet.getMaximumNumberOfItems());
		
		for (int i=1; i<=_dataSet.getMaximumNumberOfItems(); i++) {
			Item item1 = _dataSet.getItem(i);
			
			for (int j=_dataSet.getMaximumNumberOfItems(); j > i ; j--) {
				Item item2 = _dataSet.getItem(j);
				
				matrix.set(i, j, computeDistance(item1, item2));
			}
		}
		
		return matrix;	
	}


	private float computeDistance(Item item1, Item item2) {
		float weightedSum = 0f;
		float sumOfWeights = 0f;
		for (int i=1; i<=_dataSet.getNumberOfCharacters(); i++) {
			if (_context.isCharacterExcluded(i)) {
				continue;
			}
			Character character = _dataSet.getCharacter(i);
			// The weight values are stored as reliabilities but the values are
			// not modified.
			float weight = character.getReliability();
			Attribute attribute1 = item1.getAttribute(character);
			Attribute attribute2 = item2.getAttribute(character);
			if (attribute1.isUnknown() || attribute2.isUnknown()) {
				continue;
			}
			float distance;
			if (character.getCharacterType().isNumeric()) {
				distance = computeCharacterDifference((NumericAttribute)attribute1, (NumericAttribute)attribute2);
			}
			else {
				distance = _multistateDifferenceCalculator.computeMultiStateDifference((MultiStateAttribute)attribute1, (MultiStateAttribute)attribute2);
			}
			weightedSum += weight * distance;
			sumOfWeights += weight;
		}
		return weightedSum / sumOfWeights;
	}


	private float computeCharacterDifference(NumericAttribute attribute1, NumericAttribute attribute2) {
		int charNum = attribute1.getCharacter().getCharacterId();
		float range = _ranges.get(charNum-1);
		if (range == 0) {
			return 0;
		}
		
		return Math.abs(getSingleValue(attribute1) - getSingleValue(attribute2)) / range;
	}
	
	private float getSingleValue(NumericAttribute attribute) {
		List<NumericRange> values = attribute.getNumericValue();
		// if the attribute is not unknown we know it has 
		// a single value as CONFOR calculates it.
		return values.get(0).getNormalRange().getMaximumFloat();
	}
}
