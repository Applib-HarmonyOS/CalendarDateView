[![Build](https://github.com/applibgroup/MaterialCalendar/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/MaterialCalendar/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_MaterialCalendar&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_MaterialCalendar)

# Calendar Date View
An Android date selection control, supporting unlimited sliding.
<p>
	<image src = "/demo.png" width = 320 height = 512)/></image>
</p>

# Source
This library has been inspired by [xingxing-yan\\CalendarDateView](https://github.com/xingxing-yan/CalendarDateView).

## Integration

1. For using MaterialCalendar module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.
```
 implementation project(path: ':library')
```
2. For using MaterialCalendar module in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```
 implementation fileTree(dir: 'libs', include: ['*.har'])
```
3. For using MaterialCalendar module from a remote repository in separate application, add the below dependencies in entry/build.gradle file.
```
implementation 'dev.applibgroup:calendardateview:1.0.0'
```

## Usage
 1. Add CalendarView into your layouts or view hierarchy.

Example:

```xml
    <com.yyx.library.WeekDayView
           android:id="@+id/main_wdv"
           android:layout_width="match_parent"
           android:layout_height="40dp"
           app:top_line_color="@color/black"
           app:bottom_line_color="@color/black"
           app:work_day_color="@color/black"
           app:weekend_day_color="@android:color/holo_red_light" />
        
    <com.yyx.library.CalendarDateView
            android:id="@+id/main_cdv"
            android:layout_width="match_parent"
            android:layout_height="300dp"
            app:current_day_color="@android:color/holo_red_light"
            app:day_color="@color/black"
            app:select_day_color="@color/white"
            app:select_day_bg_color="@android:color/holo_green_light"
            app:shape_type="circle"/>
```
Check the example app for more information.

## License

	Copyright (c) Josue Mavarez 2017

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

