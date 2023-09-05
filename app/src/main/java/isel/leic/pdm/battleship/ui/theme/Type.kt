package isel.leic.pdm.battleship.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import isel.leic.pdm.battleship.R


// https://www.fontspace.com/monday-feelings-font-f88501
private val Battleship = FontFamily(
    Font(R.font.mondayfellings)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Battleship,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 42.sp
    ),

    button = TextStyle(
        fontFamily = Battleship,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 24.sp,
    ),

    /* Other default text styles to override
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)