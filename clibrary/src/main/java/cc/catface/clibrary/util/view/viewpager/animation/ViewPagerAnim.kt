package cc.catface.clibrary.util.view.viewpager.animation

import cc.catface.clibrary.util.view.viewpager.animation.define.*

/**
 * Created by Administrator on 2017/12/2/002.
 */
object ViewPagerAnim {

    val accordion = AccordionTransformer()

    val background2Foreground = BackgroundToForegroundTransformer()

    val cubeIn = CubeInTransformer()

    val cubeOut = CubeOutTransformer()

    val DEPTH_PAGE = DepthPageTransformer()

    val depthPage2 = DepthPageTransformer2()

    val drawFromBack = DrawFromBackTransformer()

    val flipHorizontal = FlipHorizontalTransformer()

    val flipVertical = FlipVerticalTransformer()

    val foregroundToBackground = ForegroundToBackgroundTransformer()

    val parallax = ParallaxPageTransformer(20)

    val rotateDown = RotateDownTransformer()

    val rotateUp = RotateUpTransformer()

    val stack = StackTransformer()

    val tablet = TabletTransformer()

    val zoomIn = ZoomInTransformer()

    val zoomOutPage = ZoomOutPageTransformer()

    val zoomOutSlide = ZoomOutSlideTransformer()

    val zoomOut = ZoomOutTranformer()

}