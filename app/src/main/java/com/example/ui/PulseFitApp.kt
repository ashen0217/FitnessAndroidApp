package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun PulseFitApp(viewModel: WorkoutViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isLoggedIn = userProfile?.isLoggedIn ?: false
    val isRegistering by viewModel.isRegistering.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
    ) {
        // Subtle ambient atmospheric header glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(ElectricLime.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width / 2f, -100f),
                    radius = size.width * 1.2f
                ),
                radius = size.width * 1.2f,
                center = Offset(size.width / 2f, -100f)
            )
        }

        AnimatedContent(
            targetState = isLoggedIn,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "auth_screen_switch"
        ) { loggedIn ->
            if (loggedIn) {
                MainHudLayout(viewModel)
            } else {
                if (isRegistering) {
                    SignUpScreen(viewModel)
                } else {
                    LoginScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: WorkoutViewModel) {
    var email by remember { mutableStateOf("athlete@pulsefit.com") }
    var password by remember { mutableStateOf("pulsepass") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?q=80&w=2069&auto=format&fit=crop")
                .crossfade(true)
                .build(),
            contentDescription = "Athlete Training",
            modifier = Modifier
                .fillMaxSize()
                .blur(4.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MidnightBackground.copy(alpha = 0.5f), MidnightBackground)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Logo Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "PulseFit Icon",
                    tint = ElectricLime,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PulseFit",
                    style = MaterialTheme.typography.displayLarge.copy(fontStyle = FontStyle.Italic),
                    color = ElectricLime,
                    fontWeight = FontWeight.Black
                )
            }

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Fuel your fire. Track your progress.",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email
                    Column {
                        Text(
                            text = "EMAIL ADDRESS",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("athlete@pulsefit.com", color = OnSurfaceVariant.copy(alpha = 0.4f)) },
                            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Mail", tint = OnSurfaceVariant) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OnSurface,
                                unfocusedTextColor = OnSurface,
                                focusedBorderColor = BrightCyan,
                                unfocusedBorderColor = OutlineColor,
                                focusedContainerColor = SurfaceContainerLow,
                                unfocusedContainerColor = SurfaceContainerLow
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Password
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PASSWORD",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                            )
                            Text(
                                text = "Forgot?",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrightCyan,
                                modifier = Modifier
                                    .clickable { }
                                    .padding(bottom = 6.dp)
                            )
                        }
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••", color = OnSurfaceVariant.copy(alpha = 0.4f)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = OnSurfaceVariant) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = OnSurfaceVariant
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OnSurface,
                                unfocusedTextColor = OnSurface,
                                focusedBorderColor = BrightCyan,
                                unfocusedBorderColor = OutlineColor,
                                focusedContainerColor = SurfaceContainerLow,
                                unfocusedContainerColor = SurfaceContainerLow
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                    }

                    // Log in button
                    Button(
                        onClick = { viewModel.loginUser(email, "Ashen Athlete") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricLime,
                            contentColor = MidnightBackground
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Login to HUD",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.Login, contentDescription = "Log in", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.White.copy(alpha = 0.10f)
                )
                Text(
                    text = "OR CONNECT",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.White.copy(alpha = 0.10f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.loginUser("google.athlete@pulsefit.com", "Google Athlete") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceContainerLow, contentColor = OnSurface),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Google sign-in tag",
                        tint = BrightCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Google", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = { viewModel.loginUser("apple.athlete@pulsefit.com", "Apple Athlete") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceContainerLow, contentColor = OnSurface),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneIphone,
                        contentDescription = "Apple sign-in tag",
                        tint = OnSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apple", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text(
                    text = "New to the grind? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
                Text(
                    text = "Initialize Account",
                    style = MaterialTheme.typography.labelLarge,
                    color = ElectricLime,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { viewModel.setRegistering(true) }
                        .testTag("go_to_register")
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SignUpScreen(viewModel: WorkoutViewModel) {
    var name by remember { mutableStateOf("Athlete Name") }
    var email by remember { mutableStateOf("athlete@example.com") }
    var password by remember { mutableStateOf("securepass") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "PulseFit Icon",
                    tint = ElectricLime,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PulseFit",
                    style = MaterialTheme.typography.displayLarge.copy(fontStyle = FontStyle.Italic),
                    color = ElectricLime,
                    fontWeight = FontWeight.Black
                )
            }

            Text(
                text = "Start Your Journey",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Precision tracking. Explosive results.",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = "FULL NAME",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Athlete Name", color = OnSurfaceVariant.copy(alpha = 0.4f)) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person", tint = OnSurfaceVariant) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OnSurface,
                                unfocusedTextColor = OnSurface,
                                focusedBorderColor = BrightCyan,
                                unfocusedBorderColor = OutlineColor,
                                focusedContainerColor = SurfaceContainerLow,
                                unfocusedContainerColor = SurfaceContainerLow
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "EMAIL ADDRESS",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("athlete@example.com", color = OnSurfaceVariant.copy(alpha = 0.4f)) },
                            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Mail", tint = OnSurfaceVariant) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_sign_up_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OnSurface,
                                unfocusedTextColor = OnSurface,
                                focusedBorderColor = BrightCyan,
                                unfocusedBorderColor = OutlineColor,
                                focusedContainerColor = SurfaceContainerLow,
                                unfocusedContainerColor = SurfaceContainerLow
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "PASSWORD",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••", color = OnSurfaceVariant.copy(alpha = 0.4f)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = OnSurfaceVariant) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_sign_up_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = OnSurface,
                                unfocusedTextColor = OnSurface,
                                focusedBorderColor = BrightCyan,
                                unfocusedBorderColor = OutlineColor,
                                focusedContainerColor = SurfaceContainerLow,
                                unfocusedContainerColor = SurfaceContainerLow
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }

                    Button(
                        onClick = { viewModel.loginUser(email, name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricLime,
                            contentColor = MidnightBackground
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Create Account",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = "Proceed", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
                Text(
                    text = "Log in",
                    style = MaterialTheme.typography.labelLarge,
                    color = BrightCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { viewModel.setRegistering(false) }
                        .testTag("go_to_login")
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MainHudLayout(viewModel: WorkoutViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    var showSettingsSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PulseFitHeader(onSettingsClick = { showSettingsSheet = true })
        },
        bottomBar = {
            PulseFitBottomBar(
                selectedTabIndex = currentTab,
                onTabSelect = { viewModel.selectTab(it) }
            )
        },
        containerColor = MidnightBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    slideInHorizontally { if (it > currentTab) 1000 else -1000 } + fadeIn() togetherWith
                            slideOutHorizontally { if (it > currentTab) -1000 else 1000 } + fadeOut()
                },
                label = "tab_switching_anim"
            ) { tab ->
                when (tab) {
                    0 -> HomeDashboardTab(viewModel)
                    1 -> WorkoutsLibraryTab(viewModel)
                    2 -> ActiveTrackingTab(viewModel)
                    3 -> StatsInsightsTab(viewModel)
                    4 -> ProfileSettingsTab(viewModel)
                }
            }
        }
    }

    if (showSettingsSheet) {
        AlertDialog(
            onDismissRequest = { showSettingsSheet = false },
            title = {
                Text("PulseFit Settings HUD", style = MaterialTheme.typography.headlineMedium, color = ElectricLime)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Manually override athletic state elements to preview layout reactive metrics.", color = OnSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                    
                    Button(
                        onClick = {
                            viewModel.triggerSeeding()
                            showSettingsSheet = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh, contentColor = OnSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Seed DB Seeding Records", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            viewModel.clearAllHistory()
                            showSettingsSheet = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PulseRed.copy(alpha = 0.2f), contentColor = PulseRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Reset / Wipe History Log", fontWeight = FontWeight.SemiBold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsSheet = false }) {
                    Text("Close HUD", color = ElectricLime)
                }
            },
            containerColor = MidnightSurface,
            textContentColor = OnSurface
        )
    }
}

@Composable
fun PulseFitHeader(onSettingsClick: () -> Unit) {
    Surface(
        color = MidnightBackground.copy(alpha = 0.85f),
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = Color.White.copy(alpha = 0.08f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1f
                )
            }
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.dp, ElectricLime.copy(alpha = 0.30f), CircleShape)
                    .background(SurfaceContainerHigh)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://lh3.googleusercontent.com/aida-public/AB6AXuD8u4KxlMC0EVU-zpwf263k8AFFVYjkziDxl6GlvoGbxgwI58L-GznTp9cD2U69r3NJOXU6IgiZhlTq02_6KW9WIpZU6Ni3InzGDNWRGajHBLEHH62VPC5yjVVmdEGvozKOUxzlT-dTWDDk7K9gkU4G9Lo7uB_2MhDlEa54vrBeQWRkxcMKYzyQCKswE1ll_jV0NIAwyj6GXFTOAGUb62__k4Fn52DBlAXMZyNIfc9A0HKQRIaHNOx3oh8-H2QUcuV9_L5Wr-VfMSU")
                        .crossfade(true)
                        .build(),
                    contentDescription = "User profile athlete round avatar photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "PulseFit",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 24.sp, fontStyle = FontStyle.Italic),
                color = ElectricLime,
                fontWeight = FontWeight.Black,
                modifier = Modifier.clickable { onSettingsClick() }
            )

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon HUD config panel",
                    tint = ElectricLime,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PulseFitBottomBar(selectedTabIndex: Int, onTabSelect: (Int) -> Unit) {
    Surface(
        color = MidnightBackground.copy(alpha = 0.90f),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .drawBehind {
                drawLine(
                    color = Color.White.copy(alpha = 0.08f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1f
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                TabItem("Home", Icons.Default.Dashboard, Icons.Outlined.Dashboard, 0),
                TabItem("Workouts", Icons.Default.FitnessCenter, Icons.Outlined.FitnessCenter, 1),
                TabItem("Active", Icons.Default.Timer, Icons.Outlined.Timer, 2),
                TabItem("Stats", Icons.Default.Insights, Icons.Outlined.Insights, 3),
                TabItem("Profile", Icons.Default.Person, Icons.Outlined.Person, 4)
            )

            tabs.forEach { tab ->
                val isActive = selectedTabIndex == tab.index
                val contentColor = if (isActive) ElectricLime else OnSurfaceVariant

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isActive) ElectricLime.copy(alpha = 0.10f) else Color.Transparent)
                        .clickable { onTabSelect(tab.index) }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isActive) tab.filledIcon else tab.outlinedIcon,
                        contentDescription = tab.name,
                        tint = contentColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = tab.name,
                        color = contentColor,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
                    )
                }
            }
        }
    }
}

data class TabItem(
    val name: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val index: Int
)

// ================= TAB 0: HOME DASHBOARD =================
@Composable
fun HomeDashboardTab(viewModel: WorkoutViewModel) {
    val plans by viewModel.allPlans.collectAsState()
    val listScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(listScrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Text(
            text = "Activity",
            style = MaterialTheme.typography.headlineLarge,
            color = OnSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {
                val strokeWidth = 11.dp.toPx()
                val size = Size(size.width - strokeWidth, size.height - strokeWidth)
                val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                drawArc(
                    color = Color.White.copy(alpha = 0.05f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = size,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color.White.copy(alpha = 0.05f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth * 1.5f, strokeWidth * 1.5f),
                    size = Size(size.width - strokeWidth * 2f, size.height - strokeWidth * 2f),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color.White.copy(alpha = 0.05f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth * 2.5f, strokeWidth * 2.5f),
                    size = Size(size.width - strokeWidth * 4f, size.height - strokeWidth * 4f),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                drawArc(
                    color = ElectricLime,
                    startAngle = -90f,
                    sweepAngle = 288f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = size,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                drawArc(
                    color = BrightCyan,
                    startAngle = -90f,
                    sweepAngle = 252f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth * 1.5f, strokeWidth * 1.5f),
                    size = Size(size.width - strokeWidth * 2f, size.height - strokeWidth * 2f),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                drawArc(
                    color = PulseRed,
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth * 2.5f, strokeWidth * 2.5f),
                    size = Size(size.width - strokeWidth * 4f, size.height - strokeWidth * 4f),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "840",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 50.sp),
                    color = ElectricLime,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "KCAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendChip("Move", ElectricLime)
            Spacer(modifier = Modifier.width(16.dp))
            LegendChip("Train", BrightCyan)
            Spacer(modifier = Modifier.width(16.dp))
            LegendChip("Stand", PulseRed)
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Today's Plan",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "SEE ALL",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricLime,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.selectTab(1) }
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                item {
                    PlanWorkoutCard(
                        categoryTag = "HIIT",
                        title = "Velocity Sprint",
                        durationSpec = "45 Min • High Intensity",
                        scheduledTime = "16:00",
                        accentColor = ElectricLime,
                        isCompleted = false,
                        onActionClick = {
                            viewModel.startWorkoutSession("Velocity Sprint", "HIIT")
                        }
                    )
                }

                item {
                    PlanWorkoutCard(
                        categoryTag = "Strength",
                        title = "Upper Body Push",
                        durationSpec = "60 Min • Hypertrophy",
                        scheduledTime = "Completed",
                        accentColor = BrightCyan,
                        isCompleted = true,
                        onActionClick = {}
                    )
                }
            }
        }

        Column {
            Text(
                text = "Live Stats",
                style = MaterialTheme.typography.headlineMedium,
                color = OnSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                HeartRateBentoCard(viewModel)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SleepCompactCard(modifier = Modifier.weight(1f))
                    RecoveryCompactCard(modifier = Modifier.weight(1f))
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun LegendChip(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
    }
}

@Composable
fun PlanWorkoutCard(
    categoryTag: String,
    title: String,
    durationSpec: String,
    scheduledTime: String,
    accentColor: Color,
    isCompleted: Boolean,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .height(170.dp)
            .border(1.dp, accentColor.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.80f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f))
                        .border(1.dp, accentColor.copy(alpha = 0.3f), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 3.dp)
                ) {
                    Text(text = categoryTag, style = MaterialTheme.typography.labelSmall, color = accentColor, fontWeight = FontWeight.Bold)
                }

                Icon(
                    imageVector = if (categoryTag == "HIIT") Icons.Default.Schedule else Icons.Default.FitnessCenter,
                    contentDescription = "Workout Category specifier icon",
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = OnSurface)
                Text(text = durationSpec, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp), color = OnSurfaceVariant)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color.White.copy(alpha = 0.05f),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1f
                        )
                    }
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = scheduledTime, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                if (isCompleted) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = "Completed mark check svg", tint = ElectricLime, modifier = Modifier.size(20.dp))
                } else {
                    Button(
                        onClick = onActionClick,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = MidnightBackground),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("start_workout_button"),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Start", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun HeartRateBentoCard(viewModel: WorkoutViewModel) {
    val liveHr by viewModel.liveHeartRate.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.8f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.align(Alignment.BottomEnd).size(120.dp)) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(PulseRed.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(size.width * 0.8f, size.height * 0.8f),
                        radius = size.width
                    ),
                    radius = size.width,
                    center = Offset(size.width * 0.8f, size.height * 0.8f)
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val wavePath = Path()
                val waveWidth = size.width
                val waveHeight = size.height * 0.75f
                wavePath.moveTo(0f, waveHeight)

                for (x in 0..waveWidth.toInt() step 5) {
                    val scale = if (x > waveWidth * 0.45f && x < waveWidth * 0.55f) {
                        sin((x - waveWidth * 0.45f) / (waveWidth * 0.1f) * Math.PI.toFloat()) * 26f
                    } else {
                        sin(x / waveWidth * 8f * Math.PI.toFloat()) * 3f
                    }
                    wavePath.lineTo(x.toFloat(), waveHeight - scale)
                }

                drawPath(
                    path = wavePath,
                    color = PulseRed.copy(alpha = 0.25f),
                    style = Stroke(width = 1.3.dp.toPx())
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Heart rate",
                            tint = PulseRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "HEART RATE",
                            style = MaterialTheme.typography.labelSmall,
                            color = PulseRed,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(PulseRed)
                    )
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$liveHr",
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "BPM",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SleepCompactCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(130.dp)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = "SleepTime Icon Tracker",
                    tint = BrightCyan,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "SLEEP",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrightCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "7",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface
                    )
                    Text(
                        text = "h",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 2.dp)
                    )
                    Text(
                        text = "12",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface
                    )
                    Text(
                        text = "m",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(start = 2.dp, end = 0.dp, top = 0.dp, bottom = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.05f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.85f)
                            .clip(CircleShape)
                            .background(BrightCyan)
                    )
                }
            }
        }
    }
}

@Composable
fun RecoveryCompactCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(130.dp)
            .border(1.dp, ElectricLime.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BatteryChargingFull,
                    contentDescription = "Recovery Power Metric Icon",
                    tint = ElectricLime,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "RECOVERY",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricLime,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "92%",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                    color = ElectricLime,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Optimal",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}

// ================= TAB 1: WORKOUTS LIBRARY =================
@Composable
fun WorkoutsLibraryTab(viewModel: WorkoutViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val activeCategory by viewModel.selectedCategory.collectAsState()

    val categories = listOf("All Workouts", "Cardio", "Strength", "HIIT", "Yoga")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Library",
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Find your next challenge.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search workouts, trainers, muscles...", color = OnSurfaceVariant.copy(alpha = 0.4f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = OnSurfaceVariant) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("workout_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface,
                    focusedBorderColor = ElectricLime,
                    unfocusedBorderColor = OutlineColor,
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isCatActive = activeCategory == category
                    val backgroundTabColor = if (isCatActive) ElectricLime.copy(alpha = 0.2f) else BrightCyan.copy(alpha = 0.05f)
                    val borderTabColor = if (isCatActive) ElectricLime.copy(alpha = 0.35f) else Color.Transparent
                    val textTabColor = if (isCatActive) ElectricLime else BrightCyan

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(backgroundTabColor)
                            .border(1.dp, borderTabColor, CircleShape)
                            .clickable { viewModel.selectCategory(category) }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelSmall,
                            color = textTabColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .border(1.dp, ElectricLime.copy(alpha = 0.20f), RoundedCornerShape(16.dp))
                    .clickable {
                        viewModel.startWorkoutSession("Explosive Power Builder", "Strength")
                    },
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=2070&auto=format&fit=crop")
                            .crossfade(true)
                            .build(),
                        contentDescription = "Explosive Power Builder background image athlete jumps",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.3f
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MidnightBackground.copy(alpha = 0.95f))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(PulseRed.copy(alpha = 0.20f))
                                    .border(1.dp, PulseRed.copy(alpha = 0.3f), CircleShape)
                                    .padding(horizontal = 12.dp, vertical = 3.dp)
                            ) {
                                Text("NEW", style = MaterialTheme.typography.labelSmall, color = PulseRed, fontWeight = FontWeight.Bold)
                            }

                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(ElectricLime)
                                    .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Play workouts loader", tint = MidnightBackground, modifier = Modifier.size(20.dp))
                            }
                        }

                        Column {
                            Text(
                                "Explosive Power Builder",
                                style = MaterialTheme.typography.headlineLarge,
                                color = OnSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Master the mechanics of power generation with this high-intensity functional strength routine.",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = OnSurfaceVariant,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Schedule, contentDescription = "Time", tint = BrightCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("45 Min", style = MaterialTheme.typography.labelSmall, color = BrightCyan)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocalFireDepartment, contentDescription = "Energy Intensity", tint = BrightCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Intense", style = MaterialTheme.typography.labelSmall, color = BrightCyan)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Recommended",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricLime,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }
        }

        val defaultLibraryItems = listOf(
            LibraryWorkoutItem("Kettlebell Kinetic Core", "Strength", 30, "Intermediate", "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=150"),
            LibraryWorkoutItem("Endurance Treadmill Sprints", "Cardio", 45, "Advanced", "https://images.unsplash.com/photo-1476480862126-209bfaa8edc8?q=80&w=150"),
            LibraryWorkoutItem("Active Recovery Flow", "Yoga", 20, "Beginner", "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=150")
        )

        val filteredList = defaultLibraryItems.filter {
            (activeCategory == "All Workouts" || it.category == activeCategory) &&
                    (query.isBlank() || it.title.contains(query, ignoreCase = true))
        }

        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No compatible workouts found match search criteria.",
                        color = OnSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(filteredList) { workout ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.startWorkoutSession(workout.title, workout.category)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(workout.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = workout.title,
                            modifier = Modifier
                                .width(120.dp)
                                .fillMaxHeight(),
                            contentScale = ContentScale.Crop,
                            alpha = 0.45f
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = workout.title,
                                    color = OnSurface,
                                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 15.sp),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(BrightCyan.copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(workout.category, color = BrightCyan, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), fontWeight = FontWeight.Bold)
                                }
                            }

                            Text(
                                text = "Dynamic tactical routines targeted on physical stamina optimization.",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Schedule, contentDescription = "Time", tint = OnSurfaceVariant, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${workout.duration} Min", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.SignalCellularAlt, contentDescription = "Skill level", tint = ElectricLime, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(workout.intensity, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(ElectricLime),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = MidnightBackground, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class LibraryWorkoutItem(
    val title: String,
    val category: String,
    val duration: Int,
    val intensity: String,
    val image: String
)

// ================= TAB 2: ACTIVE WORKOUT TRACKING =================
@Composable
fun ActiveTrackingTab(viewModel: WorkoutViewModel) {
    val title by viewModel.activeWorkoutTitle.collectAsState()
    val category by viewModel.activeWorkoutCategory.collectAsState()
    val targetWt by viewModel.targetWeight.collectAsState()
    val targetRp by viewModel.targetReps.collectAsState()
    val setSpec by viewModel.activeSet.collectAsState()

    val timerSecs by viewModel.timerSeconds.collectAsState()
    val isTimerActive by viewModel.isTimerRunning.collectAsState()
    val hrValue by viewModel.liveHeartRate.collectAsState()
    val calsBurned by viewModel.liveCaloriesBurned.collectAsState()

    val hours = timerSecs / 3600
    val minutes = (timerSecs % 3600) / 60
    val seconds = timerSecs % 60
    val timeLabelString = if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .size(260.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 5.dp.toPx()
                val sweepPercent = (timerSecs % 3600).toFloat() / 3600f
                val sweep = sweepPercent * 360f

                drawCircle(
                    color = SurfaceContainerHighest,
                    radius = (size.width / 2f) - (strokeWidth / 2f),
                    style = Stroke(width = strokeWidth)
                )

                drawArc(
                    color = ElectricLime,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeLabelString,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 52.sp),
                    color = ElectricLime,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "ACTIVE TIME",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(115.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.8f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = PulseRed, modifier = Modifier.size(16.dp))
                        Text("HEART RATE", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), color = OnSurfaceVariant)
                    }

                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("$hrValue", style = MaterialTheme.typography.headlineLarge, color = OnSurface, fontWeight = FontWeight.Bold)
                        Text("bpm", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp))
                    }

                    Canvas(modifier = Modifier.fillMaxWidth().height(15.dp)) {
                        val flowPath = Path()
                        flowPath.moveTo(0f, 10f)
                        flowPath.lineTo(20f, 10f)
                        flowPath.lineTo(30f, 2f)
                        flowPath.lineTo(40f, 15f)
                        flowPath.lineTo(50f, 10f)
                        flowPath.lineTo(size.width, 10f)

                        drawPath(flowPath, color = PulseRed.copy(alpha = 0.4f), style = Stroke(width = 1.dp.toPx()))
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(115.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.8f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = BrightCyan, modifier = Modifier.size(16.dp))
                        Text("CALORIES", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), color = OnSurfaceVariant)
                    }

                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("$calsBurned", style = MaterialTheme.typography.headlineLarge, color = OnSurface, fontWeight = FontWeight.Bold)
                        Text("kcal", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.05f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.65f)
                                .clip(CircleShape)
                                .background(BrightCyan)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, ElectricLime.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.80f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "CURRENT EXERCISE",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricLime,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = OnSurface,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(ElectricLime.copy(alpha = 0.12f))
                            .border(1.dp, ElectricLime.copy(alpha = 0.3f), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = setSpec,
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricLime,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Divider(color = Color.White.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Target Weight", style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
                    Text(targetWt, style = MaterialTheme.typography.labelLarge, color = OnSurface, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Target Reps", style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
                    Text(targetRp, style = MaterialTheme.typography.labelLarge, color = OnSurface, fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.toggleTimer() },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("pause_workout_button"),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrightCyan),
                border = BorderStroke(1.5.dp, BrightCyan)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = if (isTimerActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Pause work icon",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isTimerActive) "Pause" else "Resume",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = { viewModel.finishCurrentWorkout() },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("finish_workout_button"),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = MidnightBackground)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.StopCircle, contentDescription = "Stop", modifier = Modifier.size(18.dp))
                    Text("Finish", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ================= TAB 3: STATS & INSIGHTS =================
@Composable
fun StatsInsightsTab(viewModel: WorkoutViewModel) {
    val activities by viewModel.recentActivities.collectAsState()

    val totalCalories = activities.sumOf { it.caloriesBurned }
    val displayedCalories = 4250 + totalCalories

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Insights",
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your weekly performance metrics.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                "CALORIES BURNED",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    "%,d".format(displayedCalories),
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                                    color = BrightCyan,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    "kcal",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(ElectricLime.copy(alpha = 0.12f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(12.dp))
                                Text("+12% vs last week", style = MaterialTheme.typography.labelSmall, color = ElectricLime, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        BarChartColumn("M", 0.4f, false, "350")
                        BarChartColumn("T", 0.65f, true, "520")
                        BarChartColumn("W", 0.3f, false, "210")
                        BarChartColumn("T", 0.85f, true, "890")
                        BarChartColumn("F", 0.5f, false, "450")
                        BarChartColumn("S", 0.95f, true, "1100")
                        BarChartColumn("S", 0.7f, false, "680")
                    }
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Personal Bests",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PersonalBestCard(
                            title = "Longest Run",
                            value = "12.5",
                            unit = "km",
                            icon = Icons.Default.DirectionsRun,
                            iconColor = ElectricLime,
                            isNew = true,
                            modifier = Modifier.weight(1f)
                        )

                        PersonalBestCard(
                            title = "Max Deadlift",
                            value = "180",
                            unit = "kg",
                            icon = Icons.Default.FitnessCenter,
                            iconColor = BrightCyan,
                            isNew = false,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = BrightCyan, modifier = Modifier.size(16.dp))
                                Text(
                                    "TOTAL ACTIVE TIME",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrightCyan,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "14",
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                                    color = OnSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "h",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = OnSurfaceVariant,
                                    modifier = Modifier.padding(start = 2.dp, end = 2.dp, bottom = 2.dp)
                                )
                                Text(
                                    text = "30",
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                                    color = OnSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "m",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = OnSurfaceVariant,
                                    modifier = Modifier.padding(start = 2.dp, end = 0.dp, top = 0.dp, bottom = 2.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.05f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.85f)
                                        .clip(CircleShape)
                                        .background(BrightCyan)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Clear History",
                    style = MaterialTheme.typography.labelSmall,
                    color = PulseRed,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.clearAllHistory() }
                )
            }
        }

        if (activities.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No completed session recorded yet. Start training!",
                            color = OnSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(activities) { log ->
                CompletedActivityRowItem(log)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BarChartColumn(day: String, ratio: Float, isHighlighted: Boolean, kcalLabel: String) {
    var showTooltip by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (showTooltip) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(SurfaceContainerHighest)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(kcalLabel, color = OnSurface, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        Box(
            modifier = Modifier
                .weight(1f, fill = false)
                .fillMaxHeight(ratio)
                .width(22.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(
                    if (isHighlighted) {
                        Brush.verticalGradient(listOf(ElectricLime.copy(alpha = 0.2f), ElectricLime))
                    } else {
                        Brush.verticalGradient(listOf(BrightCyan.copy(alpha = 0.2f), BrightCyan))
                    }
                )
                .clickable { showTooltip = !showTooltip }
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = day,
            color = if (isHighlighted) ElectricLime else OnSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PersonalBestCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    iconColor: Color,
    isNew: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .border(1.dp, iconColor.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                }

                if (isNew) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceContainerHighest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("New", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = OnSurfaceVariant)
                    }
                }
            }

            Column {
                Text(title, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(value, style = MaterialTheme.typography.headlineMedium, color = OnSurface, fontWeight = FontWeight.Bold)
                    Text(unit, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp))
                }
            }
        }
    }
}

@Composable
fun CompletedActivityRowItem(log: CompletedWorkout) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (log.category) {
                                "HIIT" -> PulseRed.copy(alpha = 0.12f)
                                "Strength" -> ElectricLime.copy(alpha = 0.12f)
                                else -> BrightCyan.copy(alpha = 0.12f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (log.category) {
                            "HIIT" -> Icons.Default.DirectionsRun
                            "Strength" -> Icons.Default.FitnessCenter
                            else -> Icons.Default.Pool
                        },
                        contentDescription = "Session graphic icon representation",
                        tint = when (log.category) {
                            "HIIT" -> PulseRed
                            "Strength" -> ElectricLime
                            else -> BrightCyan
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(text = log.title, style = MaterialTheme.typography.labelLarge, color = OnSurface, fontWeight = FontWeight.Bold)
                    Text(text = log.intensity, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = log.displayMetricValue,
                        style = MaterialTheme.typography.headlineSmall,
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = log.displayMetricUnit,
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(CircleShape)
                        .background(ElectricLime.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(log.category, color = ElectricLime, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ================= TAB 4: PROFILE SETTINGS =================
@Composable
fun ProfileSettingsTab(viewModel: WorkoutViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()

    var weightText by remember { mutableStateOf("185") }
    var repsText by remember { mutableStateOf("8 - 10") }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            weightText = it.mTargetWeight.toString()
            repsText = it.mTargetReps
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Athlete Profile",
            style = MaterialTheme.typography.headlineLarge,
            color = OnSurface,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, ElectricLime, CircleShape)
                        .background(SurfaceContainerHigh)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://lh3.googleusercontent.com/aida-public/AB6AXuD8u4KxlMC0EVU-zpwf263k8AFFVYjkziDxl6GlvoGbxgwI58L-GznTp9cD2U69r3NJOXU6IgiZhlTq02_6KW9WIpZU6Ni3InzGDNWRGajHBLEHH62VPC5yjVVmdEGvozKOUxzlT-dTWDDk7K9gkU4G9Lo7uB_2MhDlEa54vrBeQWRkxcMKYzyQCKswE1ll_jV0NIAwyj6GXFTOAGUb62__k4Fn52DBlAXMZyNIfc9A0HKQRIaHNOx3oh8-H2QUcuV9_L5Wr-VfMSU")
                            .crossfade(true)
                            .build(),
                        contentDescription = "User profile photo large",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userProfile?.name ?: "Ashen Athlete",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurface,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = userProfile?.email ?: "ashen0217@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
            }
        }

        Text(
            text = "HUD Targeting Specifiers",
            style = MaterialTheme.typography.headlineMedium,
            color = OnSurface,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f))
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("TARGET WEIGHT (LBS)", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            focusedBorderColor = ElectricLime,
                            unfocusedBorderColor = OutlineColor,
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Column {
                    Text("TARGET REPS RANGE", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = repsText,
                        onValueChange = { repsText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            focusedBorderColor = ElectricLime,
                            unfocusedBorderColor = OutlineColor,
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow
                        ),
                        singleLine = true
                    )
                }

                Button(
                    onClick = {
                        val wtFloat = weightText.toFloatOrNull() ?: 185f
                        viewModel.updateProfileTargets(wtFloat, repsText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = MidnightBackground)
                ) {
                    Text("Save Target Specs", fontWeight = FontWeight.Bold)
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, PulseRed.copy(alpha = 0.20f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.85f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("HUD Diagnostic Controls", style = MaterialTheme.typography.labelSmall, color = PulseRed, fontWeight = FontWeight.Bold)

                Button(
                    onClick = { viewModel.clearAllHistory() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PulseRed.copy(alpha = 0.15f), contentColor = PulseRed)
                ) {
                    Text("Reset/Wipe History Database", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.logoutUser() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHighest, contentColor = OnSurface)
                ) {
                    Text("Logout of HUD Session", fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
