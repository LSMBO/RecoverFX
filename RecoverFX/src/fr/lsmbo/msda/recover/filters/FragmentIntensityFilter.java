package fr.lsmbo.msda.recover.filters;

import fr.lsmbo.msda.recover.model.ComparisonTypes;
import fr.lsmbo.msda.recover.model.Spectrum;

public class FragmentIntensityFilter implements BasicFilter {

	private float intensity;
	private ComparisonTypes comparator;
	
	public void setParameters(int _intensity, ComparisonTypes _comparator) {
		intensity = _intensity;
		comparator = _comparator;
	}
	
	@Override
	public Boolean isValid(Spectrum spectrum) {
		switch(comparator) {
		case EQUALS_TO:
			if(spectrum.getFragmentMaxIntensity() == intensity) return false; 
			break;
		case NOT_EQUALS_TO:
			if(spectrum.getFragmentMaxIntensity() != intensity) return false; 
			break;
		case GREATER_THAN:
			if(spectrum.getFragmentMaxIntensity() > intensity) return false; 
			break;
		case GREATER_OR_EQUAL:
			if(spectrum.getFragmentMaxIntensity() >= intensity) return false; 
			break;
		case LOWER_THAN:
			if(spectrum.getFragmentMaxIntensity() < intensity) return false; 
			break;
		case LOWER_OR_EQUAL:
			if(spectrum.getFragmentMaxIntensity() <= intensity) return false; 
			break;
		default: return true;
		}
		return true;
	}

	@Override
	public String getFullDescription() {
		return "";
	}

}