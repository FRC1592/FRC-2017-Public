package com.qtech.first.qLibs.joysticks;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
Left Trigger (Back)                   Right Trigger (Back)

   _.-'BUMP `-._                          _,-'BUMP'-._
,-'             `-.,__________________,.-'      .-.    `-.
/      .-Y .                ___                ( Y )      \
/    ,' .-. `.      ____   / X \   _____    .-. `-` .-.    \
/   -X |   | +X    (Back) | / \ | (Start)  ( X )   ( B )   |
/    `. `-' ,'    __       \___/            `-` ,-. `-`    |
|      `+Y `   ,-`  `-.          .-Y .         ( A )       |
|             / -'  `- \       ,'  .  `.        `-`        |
|            |    POV   |     -X -  - +X                   |
!             \ -.  ,- /       `.  '  ,'                   |
|              `-.__,-'          `+Y `                     |
|                  ________________                        /
|             _,-'`                ``-._                  /
|          ,-'                          `-.              /  Based on 10/10 ASCII ART BYosrevad
\       ,'                                 `.           /
 `.__,-'                                     `-.______,'     */


public class XboxButton extends JoystickButton{

	public enum ButtonName {
		A(1),
		B(2),
		X(3),
		Y(4),
		LEFT_BUMPER(5),
		RIGHT_BUMPER(6),
		BACK(7),
		START(8),
		LEFT_STICK(9),
		RIGHT_STICK(10),
		LEFT_TRIGGER(11),
		RIGHT_TRIGGER(12);

		public final int value;

		private ButtonName(int button) {
			this.value = button;
		}

	}

	public XboxButton(GenericHID joystick, int buttonNumber) {
		super(joystick, buttonNumber);
	}
	
	public XboxButton(GenericHID joystick, ButtonName buttonName) {
		super(joystick, buttonName.value);
	}
}
