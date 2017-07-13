package driveControl;

import virtualDevices.MotorAngleMeasure;

public class DistanceAngleController {
	private float targetSpeed;
	private float targetTurnAngle;//動かしたい角度(反時計回り)
	private WheelController wheelCtrl;
	private MotorAngleMeasure motoranglemeasure;

	private final float TIRE_CIRCUMFERENCE = 25.662F;//タイヤの円周。タイヤの直径8.16cm×3.14=25.662cm
	
	
	public DistanceAngleController()
	{
		wheelCtrl = new WheelController();
		motoranglemeasure = new MotorAngleMeasure();
		targetSpeed = 0.000F;
		targetTurnAngle = 0.000F;
	}
	
	//第一引数で移動距離、第二引数で速度
	public void GoStraightAhead(float TargetDistance , float speed)
	{
		targetSpeed = speed;
		motoranglemeasure.resetMotorAngle();
		
		//慣性を考慮した調整(大雑把)
		float adjustedDistance = speed - 40.000F;
		if(adjustedDistance < 0)
		{
			adjustedDistance = 0;
		}
		if(TargetDistance >= 0)
		{
			//とりあえずspeedが40の時は想定通りの距離で動作する それ以上の時は速さに応じて早めに止まるようになっているため、例えばspeedが100の時に10cm以下にすると大幅に狂うので注意
			while( ( ( motoranglemeasure.getMotorAngle() )[0] ) <= 360.000F * ( (TargetDistance - ( adjustedDistance * 3.000F / 20.000F) ) / TIRE_CIRCUMFERENCE) 
					|| ( ( motoranglemeasure.getMotorAngle() )[1] ) <= 360.000F * ( (TargetDistance - ( adjustedDistance * 3.000F / 20.000F) ) / TIRE_CIRCUMFERENCE) )
			{
				wheelCtrl.controlWheels(0.000F,targetSpeed);
				
			}
		}
		else
		{
			while( ( ( motoranglemeasure.getMotorAngle() )[0] ) >= 360.000F * ( (TargetDistance - ( adjustedDistance * 3.000F / 20.000F) ) / TIRE_CIRCUMFERENCE) 
					|| ( ( motoranglemeasure.getMotorAngle() )[1] ) >= 360.000F * ( (TargetDistance - ( adjustedDistance * 3.000F / 20.000F) ) / TIRE_CIRCUMFERENCE) )
			{
				wheelCtrl.controlWheelsDirect((int)-speed, (int)-speed);
				
			}
		}
	}
	
	//第一引数で動かす角度、第二引数でブロックを持っているかどうか（持っているときはtrue）
	public void Turn(float angle , boolean holdblock)
	{
		float adjustedValue = 0.000F;
		if(holdblock)
		{
			adjustedValue = 20.000F;
		}
		else
		{
			adjustedValue = 10.000F;
		}
		
		targetTurnAngle = angle;
		motoranglemeasure.resetMotorAngle();
		if(targetTurnAngle >= 0)
		{
			//TachoCountが360で180度回転するが、角度が大きくなるほど若干足りなくなったため170で割っている。ブロックを持ってる時は160で割るとちょうど良かった
			while(( ( motoranglemeasure.getMotorAngle() )[0] ) <= 360.000F * (targetTurnAngle / (180.000F - adjustedValue)))
			{
				wheelCtrl.controlWheelsDirect(64, -32);
				
			}
		}
		else
		{
			while(( ( motoranglemeasure.getMotorAngle() )[0] ) >= 360.000F * (targetTurnAngle / (180.000F - adjustedValue)))
			{
				wheelCtrl.controlWheelsDirect(-32, 64);
				
			}
		}
	}

}
