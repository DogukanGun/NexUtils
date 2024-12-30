package com.dag.nexutils.base.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dag.nexutils.R


@Composable
@Preview
fun LoadingIcon(
    modifier: Modifier = Modifier
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(composition, reverseOnRepeat = true)

    LottieAnimation(
        modifier = modifier.fillMaxSize(),
        composition = composition,
        progress = {progress}
    )
}