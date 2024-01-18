package Lab4;

public class RoomModel {
    private static final double StartingTemperature = 24.0;
    private static final double heaterConstant = 0.01;
    private static final double wallConstant = 0.00055;
    private static final double windowConstant = 0.01;
    double currentTemaprature;
    public RoomModel() {
        currentTemaprature = StartingTemperature; }
    public void updateModel(boolean heatingOn, double heaterWaterTemp, boolean windowOpen, double outSideTemp) {
        double delatHeater = (heatingOn) ? (heaterWaterTemp - currentTemaprature) : 0.0;
        double outsideDelta = currentTemaprature - outSideTemp;
        currentTemaprature += delatHeater * heaterConstant - outsideDelta * wallConstant -
                ((windowOpen) ? (outsideDelta * windowConstant) : 0.0); }
    public double getCurrentTemperature() {
        return currentTemaprature; }
}
