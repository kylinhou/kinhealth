package com.ankangcare.app.model

enum class RecordType(val displayName: String, val icon: String) {
    TEMPERATURE("体温", "🌡️"),
    MEDICATION("用药", "💊"),
    STOOL("排便", "💩"),
    FEEDING("喂养", "🍼"),
    URIC_ACID("尿酸", "🧪"),
    BLOOD_PRESSURE("血压", "🩺"),
    SLEEP("睡眠", "🌙")
}
