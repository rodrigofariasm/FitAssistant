package com.g14.ucd.fitassistant;
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 *
 * A set of constants used by all of the components in this application. To use these constants
 * the components implement the interface.
 */

public final class CommonConstants {

    public CommonConstants() {

        // don't allow the class to be instantiated
    }

    public static final int DEFAULT_TIMER_DURATION = 10000;
    public static final String ACTION_PERFORMED = "ACTION_PERFORMED";
    public static final String ACTION_UNPERFORMED = "ACTION_UNPERFORMED";
    public static final String ACTION_MEAL = "ACTION_MEAL";
    public static final String ACTION_EXERCISE = "ACTION_EXERCISE";
    public static final String EXTRA_MESSAGE= "EXTRA_MESSAGE";
    public static final String MEAL_TYPE= "MEALTYPE";


    public static final String EXTRA_TIMER = "pingme.EXTRA_TIMER";
    public static int NOTIFICATION_ID = 001;
    public static final String DEBUG_TAG = "PingMe";
}
