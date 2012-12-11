ParallaxScrollView
==================

A Parallax ScrollView which takes a background and foreground view, in the ParallexScrollView.

Demo
----
Please download the [demo](https://github.com/chrisjenx/ParallaxScrollView/downloads).
Bakground Image Thanks too [shoptalklondon](http://shoptalklondon.com/sky-series-natural-gradients/)

Work in progress
----------------

Please bear with me, this is a work in progress and there **will** be bugs and layout issues.

Usage
-----
Look at the [demo](https://github.com/chrisjenx/ParallaxScrollView/downloads) layout for implimentation.

The basics are, that you need two views added to the ParallaxScrollView and it will do the rest.

The first view added is the background, the second view added is the foreground. Layout and measuring is based roughly around a FrameLayout
The foreground gets wrapped with a ObservableScrollView, so make sure you either impliment the ScrollView **or** Use a ViewGroup.

Example Layout
--------------
```xml
<couk.jenxsol.parallaxscrollview.views.ParallaxScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="fitXY"
        android:src="@drawable/bg_sky" />

    <FrameLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:paddingLeft="16dp"
        android:paddingRight="16dp" >

        <TextView
            android:layout_width="300dp"
            android:layout_height="wrap_content"
            android:background="@android:color/holo_green_dark"
            android:padding="16dp"
            android:text="@string/hello_world"
            tools:ignore="NewApi" />
    </FrameLayout>

</couk.jenxsol.parallaxscrollview.views.ParallaxScrollView>
```

Licence
-------
   Copyright 2012 Christopher Jenkins

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
