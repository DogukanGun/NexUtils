package com.dag.nexutils.features.splash

import com.dag.nexutils.base.BaseVS


sealed class SplashVS: BaseVS {
    data object Default: SplashVS()
}
