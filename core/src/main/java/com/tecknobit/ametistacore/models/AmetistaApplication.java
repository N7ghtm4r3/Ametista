package com.tecknobit.ametistacore.models;

import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic;
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AmetistaApplication extends AmetistaItem {

    public static final String APPLICATION_KEY = "application";

    public static final String PLATFORM_KEY = "platform";

    private final String icon;

    private final String description;

    private final Set<Platform> platforms;

    private final List<IssueAnalytic> issues;

    public AmetistaApplication() {
        this(null, null, null, null, new HashSet<>(), -1, List.of());
    }

    // TODO: 14/10/2024 TO REMOVE
    public AmetistaApplication(String name) {
        super(String.valueOf(new Random().nextInt()), name, System.currentTimeMillis());
        this.icon = "https://www.euroschoolindia.com/wp-content/uploads/2023/06/facts-about-space.jpg";
        this.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce consequat imperdiet accumsan. Quisque nisl mi, dignissim et mauris pharetra, laoreet dictum leo. Aenean efficitur lorem a nibh ullamcorper, a commodo lorem tincidunt. Integer cursus posuere tempor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas ullamcorper elit a varius placerat. Vivamus nec ex ultricies, accumsan est sed, facilisis ipsum. Vivamus condimentum lacinia mi, at ultrices purus tincidunt eget. Ut dictum mi augue, vitae maximus mi feugiat sit amet. Mauris ipsum arcu, fermentum non orci non, blandit bibendum dui. Sed nulla justo, posuere at lectus venenatis, ullamcorper porttitor odio. Cras sed dolor turpis. Duis eu varius mauris, at euismod enim. Nulla facilisi. Pellentesque consequat venenatis tortor id aliquam.";
        HashSet<Platform> platforms = new HashSet<>();
        for (Platform platform : Platform.values())
            if (new Random().nextBoolean())
                platforms.add(platform);
        this.platforms = platforms;
        String issue = "FATAL EXCEPTION: main\n" +
                "Process: com.tecknobit.ametista, PID: 5513\n" +
                "java.lang.NullPointerException\n" +
                "\tat com.tecknobit.ametista.ComposableSingletons$AppKt$lambda-4$1.invoke(App.kt:121)\n" +
                "\tat com.tecknobit.ametista.ComposableSingletons$AppKt$lambda-4$1.invoke(App.kt:119)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:139)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:35)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt.ComposeContent(NavHost.kt:311)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt.access$ComposeContent(NavHost.kt:1)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHostContent$1$1.invoke(NavHost.kt:285)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHostContent$1$1.invoke(NavHost.kt:284)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:109)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:35)\n" +
                "\tat androidx.compose.runtime.CompositionLocalKt.CompositionLocalProvider(CompositionLocal.kt:380)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHostContent$1.invoke(NavHost.kt:280)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHostContent$1.invoke(NavHost.kt:279)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:109)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:35)\n" +
                "\tat androidx.compose.runtime.CompositionLocalKt.CompositionLocalProvider(CompositionLocal.kt:401)\n" +
                "\tat androidx.compose.runtime.saveable.SaveableStateHolderImpl.SaveableStateProvider(SaveableStateHolder.kt:85)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt.NavHostContent(NavHost.kt:279)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt.access$NavHostContent(NavHost.kt:1)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHost$6$1$3.invoke(NavHost.kt:258)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHost$6$1$3.invoke(NavHost.kt:257)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:139)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:35)\n" +
                "\tat androidx.compose.animation.AnimatedContentKt$AnimatedContent$6$1$5.invoke(AnimatedContent.kt:803)\n" +
                "\tat androidx.compose.animation.AnimatedContentKt$AnimatedContent$6$1$5.invoke(AnimatedContent.kt:792)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:118)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:35)\n" +
                "\tat androidx.compose.animation.AnimatedVisibilityKt.AnimatedEnterExitImpl(AnimatedVisibility.kt:771)\n" +
                "\tat androidx.compose.animation.AnimatedContentKt$AnimatedContent$6$1.invoke(AnimatedContent.kt:774)\n" +
                "\tat androidx.compose.animation.AnimatedContentKt$AnimatedContent$6$1.invoke(AnimatedContent.kt:757)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:109)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:35)\n" +
                "\tat androidx.compose.animation.AnimatedContentKt.AnimatedContent(AnimatedContent.kt:816)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHost$6.invoke(NavHost.kt:254)\n" +
                "\tat moe.tlaster.precompose.navigation.NavHostKt$NavHost$6.invoke(NavHost.kt:138)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.jvm.kt:118)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl$invoke$1.invoke(ComposableLambda.jvm.kt:130)\n" +
                "\tat androidx.compose.runtime.internal.ComposableLambdaImpl$invoke$1.invoke(ComposableLambda.jvm.kt:129)\n" +
                "\tat androidx.compose.runtime.RecomposeScopeImpl.compose(RecomposeScopeImpl.kt:192)\n" +
                "\tat androidx.compose.runtime.ComposerImpl.recomposeToGroupEnd(Composer.kt:2825)\n" +
                "\tat androidx.compose.runtime.ComposerImpl.skipCurrentGroup(Composer.kt:3116)\n" +
                "\tat androidx.compose.runtime.ComposerImpl.doCompose(Composer.kt:3607)\n" +
                "\tat androidx.compose.runtime.ComposerImpl.recompose$runtime_release(Composer.kt:3552)\n" +
                "\tat androidx.compose.runtime.CompositionImpl.recompose(Composition.kt:948)\n" +
                "\tat androidx.compose.runtime.Recomposer.performRecompose(Recomposer.kt:1206)\n" +
                "\tat androidx.compose.runtime.Recomposer.access$performRecompose(Recomposer.kt:132)\n" +
                "\tat androidx.compose.runtime.Recomposer$runRecomposeAndApplyChanges$2$1.invoke(Recomposer.kt:616)\n" +
                "\tat androidx.compose.runtime.Recomposer$runRecomposeAndApplyChanges$2$1.invoke(Recomposer.kt:585)\n" +
                "\tat androidx.compose.ui.platform.AndroidUiFrameClock$withFrameNanos$2$callback$1.doFrame(AndroidUiFrameClock.android.kt:41)\n" +
                "\tat androidx.compose.ui.platform.AndroidUiDispatcher.performFrameDispatch(AndroidUiDispatcher.android.kt:109)\n" +
                "\tat androidx.compose.ui.platform.AndroidUiDispatcher.access$performFrameDispatch(AndroidUiDispatcher.android.kt:41)\n" +
                "\tat androidx.compose.ui.platform.AndroidUiDispatcher$dispatchCallback$1.doFrame(AndroidUiDispatcher.android.kt:69)\n" +
                "\tat android.view.Choreographer$CallbackRecord.run(Choreographer.java:1337)\n" +
                "\tat android.view.Choreographer$CallbackRecord.run(Choreographer.java:1348)\n" +
                "\tat android.view.Choreographer.doCallbacks(Choreographer.java:952)\n" +
                "\tat android.view.Choreographer.doFrame(Choreographer.java:878)\n" +
                "\tat android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:1322)\n" +
                "\tat android.os.Handler.handleCallback(Handler.java:958)\n" +
                "\tat android.os.Handler.dispatchMessage(Handler.java:99)\n" +
                "\tat android.os.Looper.loopOnce(Looper.java:205)\n" +
                "\tat android.os.Looper.loop(Looper.java:294)\n" +
                "\tat android.app.ActivityThread.main(ActivityThread.java:8177)\n" +
                "\tat java.lang.reflect.Method.invoke(Native Method)\n" +
                "\tat com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:552)\n" +
                "\tat com.android.internal.os.ZygoteInit.main(ZygoteInit.java:971)\n" +
                "\tSuppressed: kotlinx.coroutines.internal.DiagnosticCoroutineContextException: [androidx.compose.runtime.PausableMonotonicFrameClock@33f4fe8, androidx.compose.ui.platform.MotionDurationScaleImpl@1bde601, StandaloneCoroutine{Cancelling}@fb56ca6, AndroidUiDispatcher@5be06e7]\n";
        issues = List.of(new IssueAnalytic(
                        String.valueOf(new Random().nextLong()),
                        System.currentTimeMillis(),
                        "1.0.0",
                        new AmetistaAnalytic.AmetistaDevice(
                                String.valueOf(new Random().nextInt()),
                                "Brand",
                                "XL",
                                "Android",
                                "12"
                        ),
                        issue,
                        Platform.ANDROID
                ),
                new WebIssueAnalytic(
                        String.valueOf(new Random().nextLong()),
                        System.currentTimeMillis(),
                        "1.0.0",
                        new AmetistaAnalytic.AmetistaDevice(
                                String.valueOf(new Random().nextInt()),
                                "Brand",
                                "XL",
                                "Android",
                                "12"
                        ),
                        issue,
                        Platform.WEB,
                        "Chrome",
                        "122"
                ));
    }

    public AmetistaApplication(String id, String icon, String name, String description, Set<Platform> platforms,
                               long creationDate, List<IssueAnalytic> issues) {
        super(id, name, creationDate);
        this.icon = icon;
        this.description = description;
        this.platforms = platforms;
        this.issues = issues;
    }

    public AmetistaApplication(JSONObject jApplication) {
        super(jApplication);
        // TODO: 14/10/2024 TO INIT CORRECTLY
        icon = null;
        description = null;
        platforms = null;
        issues = null;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public List<IssueAnalytic> getIssues() {
        return issues;
    }

}
