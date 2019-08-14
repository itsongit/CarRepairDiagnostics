package com.ubiquisoft.evaluation.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Car {

	private String year;
	private String make;
	private String model;

	private List<Part> parts;

	/**
	 * It calculates an internal maps of how many part types are present in the car object and their corresponding count.
	 * It compares it with a reference model to see which ones of part types from the reference model are missing from
	 * the actual part types found in the car object.
	 *
	 * Assumptions made: the returned Map<PartType, Integer> contains an object only is it's missing and corresponding
	 * count of missing parts. For example, if car is missing an Engine, 3 tires and and nothing else,
	 * the returned Map will contains PartType.ENGINE, count 1, PartType.TIRE and count 3
	 *
	 * @return Map <PartType,Integer> - map of missing parts - keys are part types and values are number of those part types
	 */
	public Map<PartType, Integer> getMissingPartsMap() {
		/*
		 * Return map of the part types missing.
		 *
		 * Each car requires one of each of the following types:
		 *      ENGINE, ELECTRICAL, FUEL_FILTER, OIL_FILTER
		 * and four of the type: TIRE
		 *
		 * Example: a car only missing three of the four tires should return a map like this:
		 *
		 *      {
		 *          "TIRE": 3
		 *      }
		 */

		Map<PartType, Integer> missingPartType = new HashMap<>();

		if(null != parts){


			Hashtable<PartType, Integer> referenceModel = setupWorkingCarReference();


			Map<PartType, Integer> frequencyMap = new HashMap<>();
			for (Part part: parts) {
				PartType partType = part.getType();
				Integer count = frequencyMap.get(partType);
				if (count == null)
					count = 0;

				frequencyMap.put(partType, count + 1);
			}


			for (Map.Entry<PartType, Integer> entry : referenceModel.entrySet()) {

				PartType partType = entry.getKey();
				Integer modelCount = entry.getValue();


				boolean actualPartTypeExists = frequencyMap.containsKey(partType);

				if(actualPartTypeExists){
					//The part type exists for car object
					Integer actualCount  = frequencyMap.get(partType);
					//if the number of part type is same as what should be in the model car object, it's not missing,
					// it doesn't need to be stored in Map that will be eventually returned from this method
					if(modelCount == actualCount){
						//can be removed later, leaving it in there as it has accompanying comments for clarity
					}else if(modelCount - actualCount > 0){
						missingPartType.put(partType, modelCount - actualCount);

					}
				}else{ // this particular part type was not found in the actual car object, so it's missing and the
					// count can be whatever the model contains
					missingPartType.put(partType, modelCount);
				}

			}

		}



		return missingPartType;
	}

	@Override
	public String toString() {
		return "Car{" +
				       "year='" + year + '\'' +
				       ", make='" + make + '\'' +
				       ", model='" + model + '\'' +
				       ", parts=" + parts +
				       '}';
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters *///region
	/* --------------------------------------------------------------------------------------------------------------- */

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<Part> getParts() {
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters End *///endregion
	/* --------------------------------------------------------------------------------------------------------------- */

	/**
	 * Setting up a reference model of working car with the required parts and their part counts
	 * @return
	 */
	private Hashtable<PartType,Integer> setupWorkingCarReference() {
		Hashtable<PartType, Integer> workingModel = new Hashtable<PartType,Integer>();

		workingModel.put(PartType.ENGINE, new Integer(1));
		workingModel.put(PartType.ELECTRICAL, new Integer(1));
		workingModel.put(PartType.FUEL_FILTER, new Integer(1));
		workingModel.put(PartType.OIL_FILTER, new Integer(1));
		workingModel.put(PartType.TIRE, new Integer(4));

		return workingModel;

	}
}
