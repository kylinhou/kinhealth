package com.kinhealth.app.util

/**
 * 儿科用药安全罗盘计算器
 * 核心算法：泰诺林 (≥4-6h 间隔，24h≤4次)；美林 (≥6-8h 间隔，24h≤4次)
 */
object MedicationCompassHelper {

    enum class AntipyreticType(val displayName: String, val safeIntervalHours: Double, val maxTimesPerDay: Int) {
        TYLENOL("对乙酰氨基酚 (泰诺林)", 4.0, 4),
        MOTRIN("布洛芬 (美林)", 6.0, 4)
    }

    data class LockStatus(
        val isLocked: Boolean,
        val remainingMinutes: Long,
        val safeTimeToAdministerEpoch: Long,
        val warningMessage: String
    )

    fun checkMedicationLock(
        type: AntipyreticType,
        lastAdministeredTimestamp: Long,
        currentTimestamp: Long = System.currentTimeMillis()
    ): LockStatus {
        val intervalMillis = (type.safeIntervalHours * 3600 * 1000).toLong()
        val nextSafeEpoch = lastAdministeredTimestamp + intervalMillis
        val diffMillis = nextSafeEpoch - currentTimestamp

        return if (diffMillis > 0) {
            val remainingMin = (diffMillis / 60000) + 1
            LockStatus(
                isLocked = true,
                remainingMinutes = remainingMin,
                safeTimeToAdministerEpoch = nextSafeEpoch,
                warningMessage = "安全时间锁生效中！距离下次安全给药还需等待 $remainingMin 分钟，严防重叠用药引发肝肾毒性！"
            )
        } else {
            LockStatus(
                isLocked = false,
                remainingMinutes = 0,
                safeTimeToAdministerEpoch = nextSafeEpoch,
                warningMessage = "已达到安全时间间隔（> ${type.safeIntervalHours}小时），在体温 ≥ 38.5℃ 时可遵医嘱给药。"
            )
        }
    }
}

