package com.hamilton.weatherapp.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

private const val ANIMATION_TRANSITION_TIME = 300

data object Transitions {
    val EnterLeft: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        slideIntoContainer(
            animationSpec = tween(ANIMATION_TRANSITION_TIME, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.Left
        )
    }
    val EnterRight: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        slideIntoContainer(
            animationSpec = tween(ANIMATION_TRANSITION_TIME, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.Right
        )
    }
    val ExitRight: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        slideOutOfContainer(
            animationSpec = tween(ANIMATION_TRANSITION_TIME, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.Right
        )
    }
    val ExitLeft: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        slideOutOfContainer(
            animationSpec = tween(ANIMATION_TRANSITION_TIME, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.Left
        )
    }
}