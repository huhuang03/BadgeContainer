## Introduce
BadgeContainer is a RelativeLayout which can show a badge view on a child view or a child's child view

![samle](https://raw.githubusercontent.com/huhuang03/badgeContainer/master/img/sample1.png)

## Usage
```java
<com.tonghu.badgecontainer.BadgeContainer
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:badgeContainer_count="11"
    app:badgeContainer_target_id="@+id/targetView"
    tools:context="com.tonghu.sample.MainActivity">

    <TextView
        android:layout_marginLeft="@dimen/activity_horizontal_margin"
        android:layout_marginTop="@dimen/activity_vertical_margin"
        android:id="@+id/targetView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#00ffff"
        android:text="Hello World!"/>
</com.tonghu.badgecontainer.BadgeContainer>

```

use `app:badgeContainer_target_id` to specify the child view

## attrs
* badgeContainer_count: show the number
* badgeContainer_padding_left: x offset to left
* badgeContainer_padding_bottom: y offsert to bottom
* badgeContainer_direction: offset to outter or inner
* badgeContainer_badge_bg: the red dot's background

## gradle
`comple com.tonghu.view:badgeContainer:1.03`
