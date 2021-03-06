
package cz.GravelCZLP.TracerBlocker;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright 2016 Luuk Jacobs

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Settings {
	public static class PlayerHider {
		public static boolean enabled = true;
		public static boolean calulatef5 = false;
		public static int everyTicks = 2;
		public static int ignoreDistance = 8;
		public static int maxDistance = 50;
		public static double rtDist = 0.1;
		public static List<String> disabledWorlds = new ArrayList<>();
	}

	public static class ChestHider {
		public static boolean enabled = true;
		public static boolean calulatef5 = false;
		public static int everyTicks = 5;
		public static int ignoreDistance = 8;
		public static int maxDistance = 32;
		public static double rtDist = 0.5;
		public static List<String> disabledWorlds = new ArrayList<>();
	}

	public static class FakePlayers {
		public static boolean enabled = true;
		public static boolean moving = true;
		public static boolean showArrows = true;
		public static int everyTicks = 40;
		public static int secondsAlive = 5;
		public static int speed = 3;
		public static double maxDistance = 16;
		public static List<String> disabledWorlds = new ArrayList<>();
	}
	
	public static class Test {
		
		public static boolean antiHealthTags = true;
		public static boolean packetAntiChestEsp = false;
		public static boolean debug = false;
		
	}
}
