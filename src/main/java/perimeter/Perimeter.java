package perimeter;


import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import socket.Rio;
import vector.Vector2d;

import java.io.IOException;

public class Perimeter {


    private AHRS navx;
    private Encoder left, right;
    private double INCH_PER_TICK;

    private Rio rio;

    private double
            boundX, boundY,
            oldSec, oldDistance, oldHeading;

    private Vector2d position;


    public Perimeter(AHRS navx, Encoder left, Encoder right, double ticksPerInch, double boundX, double boundY) {
        this.navx = navx;
        this.left = left;
        this.right = right;
        this.INCH_PER_TICK = 1/ticksPerInch;
        this.boundX = boundX;
        this.boundY = boundY;
    }

    public void position() throws IOException {
        double pastSec = System.currentTimeMillis()/1000.0-oldSec;
        if(pastSec > 0.25) {
            double distance = ((left.get()*INCH_PER_TICK +
                    right.get()*INCH_PER_TICK)/2);

            double angle = navx.getYaw() - oldHeading;
            double diffDistance = distance - oldDistance;
            Vector2d change = new Vector2d(diffDistance*Math.sin(angle*Math.PI/180),
                    diffDistance*Math.cos(angle*Math.PI/180));
            position = position.add(change);
            setPositionBounds();
            setWarningBounds();
            rio.send(position);

            SmartDashboard.putNumber("Heading", angle);
            SmartDashboard.getNumber("Position X", position.x);
            SmartDashboard.getNumber("Position Y", position.y);

            oldSec = System.currentTimeMillis()/1000.0;
            oldDistance = distance;
            oldHeading = angle;
        }

    }

    public void startServer(){
        rio = new Rio();
        rio.start();
    }


    private void setPositionBounds() {

        if(position.x < 0){
            position.x = 0;
        }

        if(position.x > boundX){
            position.x = boundX;
        }

        if(position.y < 0){
            position.y = 0;
        }

        if(position.y > boundY){
            position.y = boundY;
        }
    }

    private void setWarningBounds() {

        if(position.x > boundX-10 || position.x < 10 || position.y > boundY-10 || position.y < 10){
            SmartDashboard.putBoolean("Approaching Boundary", true);
        }else{
            SmartDashboard.putBoolean("Approaching Boundary", false);
        }

    }
}
