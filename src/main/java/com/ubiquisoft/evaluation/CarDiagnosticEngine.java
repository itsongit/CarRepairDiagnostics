package com.ubiquisoft.evaluation;

import com.ubiquisoft.evaluation.domain.Car;
import com.ubiquisoft.evaluation.domain.ConditionType;
import com.ubiquisoft.evaluation.domain.Part;
import com.ubiquisoft.evaluation.domain.PartType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarDiagnosticEngine {

	public void executeDiagnostics(Car car) {
		/*
		 * Implement basic diagnostics and print results to console.
		 *
		 * The purpose of this method is to find any problems with a car's data or parts.
		 *
		 * Diagnostic Steps:
		 *      First   - Validate the 3 data fields are present, if one or more are
		 *                then print the missing fields to the console
		 *                in a similar manner to how the provided methods do.
		 *
		 *      Second  - Validate that no parts are missing using the 'getMissingPartsMap' method in the Car class,
		 *                if one or more are then run each missing part and its count through the provided missing part method.
		 *
		 *      Third   - Validate that all parts are in working condition, if any are not
		 *                then run each non-working part through the provided damaged part method.
		 *
		 *      Fourth  - If validation succeeds for the previous steps then print something to the console informing the user as such.
		 * A damaged part is one that has any condition other than NEW, GOOD, or WORN.
		 *
		 * Important:
		 *      If any validation fails, complete whatever step you are actively one and end diagnostics early.
		 *
		 * Treat the console as information being read by a user of this application. Attempts should be made to ensure
		 * console output is as least as informative as the provided methods.
		 */



		System.out.println(
				"============================================================="
		);
		System.out.println(
				"Welcome to UbiquiSoft State of the Art Car Diagnostic Center"
		);
		System.out.println(
				"============================================================="
		);


		System.out.println("Starting Diagnostic Process");

		System.out.println(">Initiating Step 1 ... Checking if Car is all good...");
		//Diagnostic Step 1:
		boolean isCarValid = validateCar(car);

		System.out.println(">Step 1 .. Complete");

		System.out.println(">>Moving to Step 2 ... Checking for any missing parts...");
		//Diagnostic Step 2:
		boolean isCarMissingParts = (isCarValid) ? checkForMissingParts(car) : true;
		System.out.println(">>Step 2 .. Complete");


		//Diagnostic Step 3:

		System.out.println(">>>Moving to Step 3 ... Checking if all parts are working as expected...");
		boolean areAllPartsWorking = (!isCarMissingParts) ? checkForWorkingParts(car) : false;
		System.out.println(">>>Step 3 .. Complete");


		StringBuffer statusMessage = new StringBuffer();


		statusMessage.append(LocalDate.now());
		statusMessage.append(" ");
		statusMessage.append(car.getYear());
		statusMessage.append(" ");
		statusMessage.append(car.getMake());
		statusMessage.append(" ");
		statusMessage.append(car.getModel());
		statusMessage.append(" ");

		if(isCarValid && !isCarMissingParts && areAllPartsWorking){
			//All good, display message on console


			statusMessage.append("was found to be free of any issues.");

			statusMessage.append(LocalDate.now());

			System.out.println(
					statusMessage.toString()
			);


		}else{

			statusMessage.append("needs further attention as the diagnostic system found some issues. ");
			System.out.println(
					statusMessage.toString()
			);

		}



	}

	private void printMissingPart(PartType partType, Integer count) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (count == null || count <= 0) throw new IllegalArgumentException("Count must be greater than 0");

		System.out.println(String.format("Missing Part(s) Detected: %s - Count: %s", partType, count));
	}

	private void printDamagedPart(PartType partType, ConditionType condition) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (condition == null) throw new IllegalArgumentException("ConditionType must not be null");

		System.out.println(String.format("Damaged Part Detected: %s - Condition: %s", partType, condition));
	}

	private void printMissingCarFields(String nameOfField) {

		if (nameOfField == null) throw new IllegalArgumentException("nameOfField must not be null");
		System.out.println("Missing Car data field Detected: " +  nameOfField);
	}

	/**
	 * This method checks three data fields in the Car object and if one or more are missing it returns false otherwise
	 * returns true
	 * @param car - Car object
	 * @return false if Car object is missing at least one of the data fields
	 * @throws IllegalArgumentException - if the car object is null
	 */
	private boolean validateCar(Car car){
		boolean isCarValid = false;
		String make = car.getMake();
		String year = car.getYear();
		String model = car.getModel();

		if(null != car){
			if( null != make || null != model || null != year){
				isCarValid = true;
			}else if(null == make){
				isCarValid = false;
				printMissingCarFields("make");
			}else if(null == year){
				isCarValid = false;
				printMissingCarFields("year");

			}else if(null == model){
				isCarValid = false;
				printMissingCarFields("model");
			}

		}else{
			throw new IllegalArgumentException("Car must not be null");
		}

		return isCarValid;

	}

	/**
	 * checkForMissingParts encapsulates the Step 2 in diagnostic steps.
	 * It checks for missing parts and if it finds any, calls printMissingPart method
	 * @param car - car object to run the disgnostic on
	 * @return true if car object is missing any parts, false if all required parts are present
	 * @throws IllegalArgumentException - if the car object is null
	 *
	 */

	private boolean checkForMissingParts(Car car){
		Map<PartType,Integer> missingPartsMap = car.getMissingPartsMap();
		boolean isCarMissingParts = false;

		if (null == car){
			throw new IllegalArgumentException("Car must not be null");
		}

		if(null != missingPartsMap){

			if (missingPartsMap.isEmpty()){
				//all part types are present including 4 tires
			}else{
				isCarMissingParts = true;
				//missingPartsMap.
				Set<Map.Entry<PartType, Integer>> entries = missingPartsMap.entrySet();

				Stream<Map.Entry<PartType, Integer>> carParts = entries.stream();

				carParts.forEach(
						entry -> printMissingPart(entry.getKey(), entry.getValue())
				);

			}
		}

	return isCarMissingParts;
	}


	/**
	 * Encapsulates Step 3 in the Diagnostic process
	 * @param car
	 * @return true if car object has all its parts working, false if any parts are not
	 * @throws IllegalArgumentException - if the car object is null
	 */
	private boolean checkForWorkingParts(Car car){
		List<Part> carParts = car.getParts();
		List<Part> damagedPartsList = null;
		boolean areAllPartsWorking = false;

		if (null == car){
			throw new IllegalArgumentException("Car must not be null");
		}

		if(null != carParts){

			damagedPartsList =
					carParts.
							stream().
							filter(
									c -> !c.isInWorkingCondition()
							).
							collect(Collectors.toList());


			damagedPartsList.
					forEach(
							damagedPart -> printDamagedPart(damagedPart.getType(),damagedPart.getCondition() )
					);

		}

		int damagedPartCount = (null != damagedPartsList) ? damagedPartsList.size() : 0;

		areAllPartsWorking = (damagedPartCount == 0) ? true : false;

		return areAllPartsWorking;
	}

	public static void main(String[] args) throws JAXBException {
		// Load classpath resource
		InputStream xml = ClassLoader.getSystemResourceAsStream("SampleCar.xml");

		// Verify resource was loaded properly
		if (xml == null) {
			System.err.println("An error occurred attempting to load SampleCar.xml");

			System.exit(1);
		}

		// Build JAXBContext for converting XML into an Object
		JAXBContext context = JAXBContext.newInstance(Car.class, Part.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Car car = (Car) unmarshaller.unmarshal(xml);

		/*
		System.out.println("Car is " + car.getMake());

		List<Part> parts = car.getParts();

		parts.stream().forEach(part -> System.out.println(part.toString()));
		*/

		// Build new Diagnostics Engine and execute on deserialized car object.

		CarDiagnosticEngine diagnosticEngine = new CarDiagnosticEngine();

		diagnosticEngine.executeDiagnostics(car);

	}

}
