package al.sybina.ebr.logbook2;

import java.time.LocalTime;

import com.jfoenix.controls.JFXChipView;

public class CodeLetterDTankWashed {

	private Integer index;
	private JFXChipView<String> identityOfTankJFXChipsView;
	private String identityTanksWashing;
	private Double numberOfMachinesInUse;
	private LocalTime timeOfStartOfWashing;
	private String washingPatternEmployed;
	private String washingPatternType;
	private Double washingLinePressureValue;
	private String washingLinePressureType;
	private LocalTime timeWashingWasCompletedOrStopped;
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public JFXChipView<String> getIdentityOfTankJFXChipsView() {
		return identityOfTankJFXChipsView;
	}
	public void setIdentityOfTankJFXChipsView(JFXChipView<String> identityOfTankJFXChipsView) {
		this.identityOfTankJFXChipsView = identityOfTankJFXChipsView;
	}
	public Double getNumberOfMachinesInUse() {
		return numberOfMachinesInUse;
	}
	public void setNumberOfMachinesInUse(Double numberOfMachinesInUse) {
		this.numberOfMachinesInUse = numberOfMachinesInUse;
	}
	public LocalTime getTimeOfStartOfWashing() {
		return timeOfStartOfWashing;
	}
	public void setTimeOfStartOfWashing(LocalTime timeOfStartOfWashing) {
		this.timeOfStartOfWashing = timeOfStartOfWashing;
	}
	public String getWashingPatternEmployed() {
		return washingPatternEmployed;
	}
	public void setWashingPatternEmployed(String washingPatternEmployed) {
		this.washingPatternEmployed = washingPatternEmployed;
	}
	public LocalTime getTimeWashingWasCompletedOrStopped() {
		return timeWashingWasCompletedOrStopped;
	}
	public void setTimeWashingWasCompletedOrStopped(LocalTime timeWashingWasCompletedOrStopped) {
		this.timeWashingWasCompletedOrStopped = timeWashingWasCompletedOrStopped;
	}
	public String getWashingPatternType() {
		return washingPatternType;
	}
	public void setWashingPatternType(String washingPatternType) {
		this.washingPatternType = washingPatternType;
	}
	public Double getWashingLinePressureValue() {
		return washingLinePressureValue;
	}
	public void setWashingLinePressureValue(Double washingLinePressureValue) {
		this.washingLinePressureValue = washingLinePressureValue;
	}
	public String getWashingLinePressureType() {
		return washingLinePressureType;
	}
	public void setWashingLinePressureType(String washingLinePressureType) {
		this.washingLinePressureType = washingLinePressureType;
	}
	public String getIdentityTanksWashing() {
		return identityTanksWashing;
	}
	public void setIdentityTanksWashing(String identityTanksWashing) {
		this.identityTanksWashing = identityTanksWashing;
	}
}
