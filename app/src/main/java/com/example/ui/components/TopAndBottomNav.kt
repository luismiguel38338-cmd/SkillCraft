package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.TechAccentGold
import com.example.ui.theme.TechPrimary
import com.example.ui.theme.TechSecondary

enum class NavTab(val title: String) {
    HOME("Ruta"),
    GAME("Juego"),
    AI_MENTOR("Mentor IA"),
    CATALOG("Catálogo"),
    COMMUNITY("Comunidad"),
    PROFILE("Perfil")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillCraftTopBar(
    userProfile: UserProfile?,
    onOpenPro: () -> Unit,
    onOpenAssessment: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(TechPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "SkillCraft",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Aprende con Proyectos Reales",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            // Streak Pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .testTag("streak_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Racha",
                        tint = Color(0xFFF97316),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${userProfile?.streakDays ?: 1}d",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Level & XP Pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = TechPrimary.copy(alpha = 0.15f),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .testTag("level_xp_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = "Nivel",
                        tint = TechPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Nv.${userProfile?.level ?: 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TechPrimary
                    )
                }
            }

            // Pro Plan Chip / Button
            if (userProfile?.isPro == true) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TechAccentGold.copy(alpha = 0.2f),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .testTag("pro_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Pro",
                            tint = TechAccentGold,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "PRO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = TechAccentGold
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TechAccentGold,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenPro() }
                        .testTag("upgrade_pro_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Plan Pro",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "PRO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Composable
fun SkillCraftBottomNavigation(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == NavTab.HOME,
            onClick = { onTabSelected(NavTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavTab.HOME) Icons.Filled.Map else Icons.Outlined.Map,
                    contentDescription = "Ruta"
                )
            },
            label = { Text("Ruta", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TechPrimary,
                selectedTextColor = TechPrimary,
                indicatorColor = TechPrimary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentTab == NavTab.GAME,
            onClick = { onTabSelected(NavTab.GAME) },
            icon = {
                BadgedBox(
                    badge = {
                        Badge(containerColor = TechAccentGold) {
                            Text("400+", fontSize = 8.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == NavTab.GAME) Icons.Filled.SportsEsports else Icons.Outlined.SportsEsports,
                        contentDescription = "Juego 400+ Niveles"
                    )
                }
            },
            label = { Text("Juego", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TechAccentGold,
                selectedTextColor = TechAccentGold,
                indicatorColor = TechAccentGold.copy(alpha = 0.2f)
            )
        )

        NavigationBarItem(
            selected = currentTab == NavTab.CATALOG,
            onClick = { onTabSelected(NavTab.CATALOG) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavTab.CATALOG) Icons.Filled.GridView else Icons.Outlined.GridView,
                    contentDescription = "Catálogo"
                )
            },
            label = { Text("Catálogo", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TechPrimary,
                selectedTextColor = TechPrimary,
                indicatorColor = TechPrimary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentTab == NavTab.AI_MENTOR,
            onClick = { onTabSelected(NavTab.AI_MENTOR) },
            icon = {
                BadgedBox(
                    badge = {
                        Badge(containerColor = TechSecondary) {
                            Text("IA", fontSize = 8.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == NavTab.AI_MENTOR) Icons.Filled.SmartToy else Icons.Outlined.SmartToy,
                        contentDescription = "Mentor IA"
                    )
                }
            },
            label = { Text("Mentor", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TechSecondary,
                selectedTextColor = TechSecondary,
                indicatorColor = TechSecondary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentTab == NavTab.COMMUNITY,
            onClick = { onTabSelected(NavTab.COMMUNITY) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavTab.COMMUNITY) Icons.Filled.Groups else Icons.Outlined.Groups,
                    contentDescription = "Comunidad"
                )
            },
            label = { Text("Social", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TechPrimary,
                selectedTextColor = TechPrimary,
                indicatorColor = TechPrimary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentTab == NavTab.PROFILE,
            onClick = { onTabSelected(NavTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Perfil"
                )
            },
            label = { Text("Perfil", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TechPrimary,
                selectedTextColor = TechPrimary,
                indicatorColor = TechPrimary.copy(alpha = 0.15f)
            )
        )
    }
}
