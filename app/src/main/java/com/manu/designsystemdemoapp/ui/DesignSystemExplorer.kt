
package com.manu.designsystemdemoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import com.manu.manudesignsystem.components.ManuButton
import com.manu.manudesignsystem.components.ManuCard
import com.manu.manudesignsystem.components.ManuChip
import com.manu.manudesignsystem.components.ManuListCard
import com.manu.manudesignsystem.components.ManuSwitch
import com.manu.manudesignsystem.components.ManuTextField
import com.manu.manudesignsystem.components.ManuTopBar
import com.manu.manudesignsystem.theme.ManuDesignSystemTheme
import kotlinx.coroutines.launch
import com.manu.manudesignsystem.components.ManuListItem
import com.manu.manudesignsystem.components.ManuMenuItem
import com.manu.manudesignsystem.components.ManuListItemData
import com.manu.manudesignsystem.components.ManuDialogBox
import com.manu.manudesignsystem.components.ManuDropdownMenu
import com.manu.manudesignsystem.components.ManuOutlineButton
import com.manu.manudesignsystem.components.ManuTextButton
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import com.manu.manudesignsystem.components.ManuBottomSheet
import com.manu.manudesignsystem.components.ManuDatePicker
import com.manu.manudesignsystem.components.ManuDateRangePicker
import com.manu.manudesignsystem.components.ManuSnackbar
import com.manu.manudesignsystem.components.ManuSnackbarType
import com.manu.manudesignsystem.components.ManuScaffold
import com.manu.manudesignsystem.components.ManuScreenContainer
import com.manu.manudesignsystem.components.ManuPickerField
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandMore


private enum class ExplorerPage(val title: String, val group: String) {
    COLORS("Colors", "FOUNDATIONS"),
    TYPOGRAPHY("Typography", "FOUNDATIONS"),
    SHAPES("Shapes", "FOUNDATIONS"),
    SPACING("Spacing", "FOUNDATIONS"),
    BUTTONS("Buttons", "COMPONENTS"),
    CARDS("Cards", "COMPONENTS"),
    TEXT_FIELDS("Text Fields", "COMPONENTS"),
    TOP_BARS("Top Bars", "COMPONENTS"),
    APPEARANCE("Appearance", "SETTINGS"),
    CHIPS("Chips", "COMPONENTS"),
    SWITCHES("Switches", "COMPONENTS"),
    LIST_ITEMS("List Items", "COMPONENTS"),
    LIST_CARDS("List Cards", "COMPONENTS"),
    DIALOG_BOX("Dialog Box", "COMPONENTS"),
    DROPDOWN_MENU("Dropdown Menu", "COMPONENTS"),
    DATE_PICKER("Date Picker", "COMPONENTS"),
    BOTTOM_SHEET("Bottom Sheet", "COMPONENTS"),
    SNACKBAR("Snackbar", "COMPONENTS"),
    SCAFFOLD("Scaffold & Screen", "COMPONENTS"),
    PICKER_FIELD("Picker Field", "COMPONENTS"),
}

@Composable
fun DesignSystemExplorer(

) {
    var selectedPage by remember { mutableStateOf(ExplorerPage.BUTTONS) }
    var darkMode by remember { mutableStateOf(false) }
    var drawerOpen by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val wideScreen = screenWidth >= 700

    ManuDesignSystemTheme(darkTheme = darkMode) {
        if (wideScreen) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ExplorerSidebar(
                    selectedPage = selectedPage,
                    onSelect = { selectedPage = it },
                    modifier = Modifier.width(230.dp)
                )

                VerticalDivider()

                ExplorerContent(
                    page = selectedPage,
                    darkMode = darkMode,
                    onToggleDarkMode = { darkMode = it },
                    onMenuClick = {}
                )
            }
        } else {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        ExplorerSidebar(
                            selectedPage = selectedPage,
                            onSelect = {
                                selectedPage = it
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                gesturesEnabled = true
            ) {
                ExplorerContent(
                    page = selectedPage,
                    darkMode = darkMode,
                    onToggleDarkMode = { darkMode = it },
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}

@Composable
private fun ExplorerSidebar(
    selectedPage: ExplorerPage,
    onSelect: (ExplorerPage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 20.dp)
    ) {
        Text(
            "MANU",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            "Design System",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(28.dp))

        listOf("FOUNDATIONS", "COMPONENTS", "SETTINGS").forEach { group ->
            Text(
                group,
                modifier = Modifier.padding(start = 12.dp, bottom = 8.dp, top = 12.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ExplorerPage.entries.filter { it.group == group }.forEach { page ->
                NavigationDrawerItem(
                    label = { Text(page.title) },
                    selected = selectedPage == page,
                    onClick = { onSelect(page) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        Text(
            "ManuDesignSystem • Demo",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ExplorerContent(
    page: ExplorerPage,
    darkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( 10.dp,35.dp,10.dp , 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onMenuClick !== {}) {
                        TextButton(onClick = onMenuClick) {
                            Text("☰")
                        }
                    }

                    Column(Modifier.weight(1f)) {
                        Text(
                            "MANU DESIGN SYSTEM",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            page.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Switch(
                        checked = darkMode,
                        onCheckedChange = onToggleDarkMode
                    )
                }
                HorizontalDivider()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 20.dp,
                    top = 32.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            when (page) {
                ExplorerPage.BUTTONS -> ButtonsPage()
                ExplorerPage.CARDS -> CardsPage()
                ExplorerPage.TEXT_FIELDS -> TextFieldsPage()
                ExplorerPage.TOP_BARS -> TopBarsPage()
                ExplorerPage.COLORS -> ColorsPage()
                ExplorerPage.TYPOGRAPHY -> TypographyPage()
                ExplorerPage.SHAPES -> ShapesPage()
                ExplorerPage.SPACING -> SpacingPage()
                ExplorerPage.APPEARANCE -> AppearancePage(
                    darkMode = darkMode,
                    onToggleDarkMode = onToggleDarkMode
                )
                ExplorerPage.CHIPS -> ChipsPage()
                ExplorerPage.SWITCHES -> SwitchesPage()
                ExplorerPage.LIST_ITEMS -> ListItemsPage()
                ExplorerPage.LIST_CARDS -> ListCardsPage()
                ExplorerPage.DIALOG_BOX -> DialogBoxPage()
                ExplorerPage.DROPDOWN_MENU -> DropdownMenuPage()
                ExplorerPage.DATE_PICKER -> DatePickersPage()
                ExplorerPage.BOTTOM_SHEET -> BottomSheetPage()
                ExplorerPage.SNACKBAR -> SnackbarPage()
                ExplorerPage.SCAFFOLD -> ScaffoldPage()
                ExplorerPage.PICKER_FIELD -> PickerFieldPage()
            }
        }
    }
}

@Composable
private fun ButtonsPage() {
    var count by remember { mutableIntStateOf(0) }

    SectionIntro("Buttons", "Reusable actions with a tactile press animation.")

    ManuCard(Modifier.fillMaxWidth()) {
        Text("Primary button", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        ManuButton(
            onClick = { count++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tap to test • $count")
        }
        Spacer(Modifier.height(8.dp))
        Text("Press the button to test its spring animation.")
    }
}

@Composable
private fun CardsPage() {
    SectionIntro("Cards", "Surfaces for grouping related content.")

    ManuCard(Modifier.fillMaxWidth()) {
        Text("Standard card", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Soft corners and comfortable internal padding.")
    }

    ManuCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text("Secondary surface", style = MaterialTheme.typography.titleMedium)
        Text("Uses a different theme surface for hierarchy.")
    }
}

@Composable
private fun TextFieldsPage() {
    var text by remember { mutableStateOf("") }

    SectionIntro("Text fields", "Test input styling and keyboard interaction.")

    ManuTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Your name") },
        placeholder = { Text("Enter your name") }
    )

    ManuCard(Modifier.fillMaxWidth()) {
        Text("Live preview", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(if (text.isBlank()) "Your text appears here." else text)
    }
}

@Composable
private fun TopBarsPage() {
    SectionIntro("Top bars", "Navigation headers for app screens.")

    ManuCard(Modifier.fillMaxWidth()) {
        ManuTopBar(title = "Example screen")
        HorizontalDivider()
        Text(
            "This is a preview of the library top bar.",
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun ColorsPage() {
    SectionIntro("Colors", "Monochrome theme colors in the current mode.")

    val scheme = MaterialTheme.colorScheme
    val swatches = listOf(
        "Primary" to scheme.primary,
        "Background" to scheme.background,
        "Surface" to scheme.surface,
        "Surface variant" to scheme.surfaceVariant,
        "Outline" to scheme.outline
    )

    swatches.forEach { (name, color) ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(56.dp)
                    .background(color, RoundedCornerShape(14.dp))
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(name, fontWeight = FontWeight.Medium)
                Text(
                    "#${color.value.toULong().toString(16).takeLast(8).uppercase()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun TypographyPage() {
    SectionIntro("Typography", "Your current type scale.")

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Text("Display headline", style = MaterialTheme.typography.headlineLarge)
        Text("Section heading", style = MaterialTheme.typography.headlineMedium)
        Text("Title", style = MaterialTheme.typography.titleLarge)
        Text("Body text for descriptions and content.", style = MaterialTheme.typography.bodyLarge)
        Text("Small supporting text", style = MaterialTheme.typography.bodySmall)
        Text("LABEL", style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun ShapesPage() {
    SectionIntro("Shapes", "Rounded geometry used by components.")

    listOf(8.dp, 12.dp, 16.dp, 24.dp, 32.dp).forEach { radius ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(64.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(radius)
                    )
            )
            Spacer(Modifier.width(16.dp))
            Text("Corner radius: $radius")
        }
    }
}

@Composable
private fun SpacingPage() {
    SectionIntro("Spacing", "A visual reference for the spacing scale.")

    listOf(4.dp, 8.dp, 12.dp, 16.dp, 24.dp, 32.dp).forEach { spacing ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                spacing.toString(),
                modifier = Modifier.width(56.dp)
            )
            Box(
                Modifier
                    .height(18.dp)
                    .width(spacing * 4)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(5.dp)
                    )
            )
        }
    }
}

@Composable
private fun AppearancePage( darkMode: Boolean, onToggleDarkMode: (Boolean) -> Unit )
{
    SectionIntro("Appearance", "Preview both theme modes.")

    ManuCard(Modifier.fillMaxWidth()) {
        Text("Dark theme", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(if (darkMode) "Currently enabled" else "Currently disabled")
        Spacer(Modifier.height(12.dp))
        Switch(
            checked = darkMode,
            onCheckedChange = onToggleDarkMode
        )
    }
}

@Composable
private fun ChipsPage() {
    var selectedChip by remember { mutableStateOf("Chip 1") }

    SectionIntro("Chips", "Selectable labels.")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        listOf("Chip 1", "Chip 2", "Chip 3").forEach { label ->
            ManuChip(
                label = label,
                selected = selectedChip == label,
                onClick = { selectedChip = label }
            )
        }
    }
}

@Composable
private fun SwitchesPage() {
    var notifications by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }

    SectionIntro("Switches", "Settings controls.")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text("Notifications")
        ManuSwitch(
            checked = notifications,
            onCheckedChange = { notifications = it }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text("Dark mode")
        ManuSwitch(
            checked = darkMode,
            onCheckedChange = { darkMode = it }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text("Disabled switch")
        ManuSwitch(
            checked = false,
            onCheckedChange = {},
            enabled = false
        )
    }
}

@Composable
private fun ListItemsPage() {
    var message by remember { mutableStateOf("No menu action yet") }

    SectionIntro("List Items", "Reusable rows with preview and actions.")

    ManuListItem(
        title = "Grocery shopping",
        description = "Food and household items",
        time = "10:30 AM",
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null
                )
            }
        },
        onClick = { message = "List item clicked" },
        menuItems = listOf(
            ManuMenuItem(
                label = "Edit",
                onClick = { message = "Edit selected" }
            ),
            ManuMenuItem(
                label = "Archive",
                onClick = { message = "Archive selected" }
            ),
            ManuMenuItem(
                label = "Delete",
                onClick = { message = "Delete selected" }
            )
        )
    )

    Text(message, style = MaterialTheme.typography.bodySmall)
}

@Composable
private fun ListCardsPage() {
    var message by remember {
        mutableStateOf("No action selected")
    }

    SectionIntro(
        "List Cards",
        "List items with automatic separators and custom menus."
    )

    ManuListCard(
        items = listOf(
            ManuListItemData(
                title = "Grocery shopping",
                description = "Food and household items",
                time = "10:30 AM",
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null
                    )
                },
                menuItems = listOf(
                    ManuMenuItem(
                        label = "Edit",
                        onClick = { message = "Edit grocery shopping" }
                    ),
                    ManuMenuItem(
                        label = "Delete",
                        onClick = { message = "Delete grocery shopping" }
                    )
                ),
                onClick = {
                    message = "Grocery shopping selected"
                }
            ),
            ManuListItemData(
                title = "Monthly expenses",
                description = "12 transactions",
                time = "Yesterday",
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null
                    )
                },
                menuItems = listOf(
                    ManuMenuItem(
                        label = "Archive",
                        onClick = { message = "Archive monthly expenses" }
                    )
                )
            )
        )
    )

    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall
    )
}
@Composable
fun DialogBoxPage() {
    var showDialog by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("No action selected") }
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Dialog & Buttons")

        ManuButton(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show dialog")
        }

        ManuTextButton(
            onClick = { result = "Text button clicked" }
        ) {
            Text("Text button")
        }

        Box {
            ManuOutlineButton(
                onClick = {
                    result = "Outline button clicked"
                    expanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open dropdown")
            }

            ManuDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                items = listOf(
                    ManuMenuItem("Edit", { result = "Edit selected" }),
                    ManuMenuItem("Duplicate", { result = "Duplicate selected" }),
                    ManuMenuItem("Delete", { result = "Delete selected" })
                )
            )
        }
        Text(result)
    }

    ManuDialogBox(
        visible = showDialog,
        title = "Confirm action",
        message = "Are you sure you want to continue?",
        confirmText = "Continue",
        dismissText = "Cancel",
        onConfirm = {
            result = "Confirmed"
            showDialog = false
        },
        onDismiss = {
            result = "Cancelled"
            showDialog = false
        }
    )
}

@Composable
private fun DropdownMenuPage() {
    var expanded by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("No action selected") }

    SectionIntro(
        "Dropdown Menu",
        "Popup menus with leading icons and actions."
    )

    Box {
        ManuButton(
            onClick = { expanded = true }
        ) {
            Text("Open menu")
        }

        ManuDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = listOf(
                ManuMenuItem(
                    label = "Edit",
                    onClick = { result = "Edit selected" },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                ),
                ManuMenuItem(
                    label = "Duplicate",
                    onClick = { result = "Duplicate selected" },
                    leadingIcon = {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = null
                        )
                    }
                ),
                ManuMenuItem(
                    label = "Delete",
                    onClick = { result = "Delete selected" },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                )
            )
        )
    }

    Text(
        text = result,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun DatePickersPage() {

    var showDatePicker by remember { mutableStateOf(false) }
    var showRangePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedRange by remember {
        mutableStateOf<Pair<Long, Long>?>(null)
    }

    ManuButton(onClick = { showDatePicker = true }) {
        Text("Choose date")
    }

    ManuDatePicker(
        expanded = showDatePicker,
        onDismissRequest = { showDatePicker = false },
        onDateSelected = { selectedDate = it }
    )

    ManuButton(onClick = { showRangePicker = true }) {
        Text("Choose date range")
    }

    ManuDateRangePicker(
        expanded = showRangePicker,
        onDismissRequest = { showRangePicker = false },
        onDateRangeSelected = { start, end ->
            selectedRange = start to end
        }
    )

}
@Composable
fun BottomSheetPage() {

    var showSheet by remember { mutableStateOf(false) }

    ManuButton(onClick = { showSheet = true }) {
        Text("Open bottom sheet")
    }

    ManuBottomSheet(
        expanded = showSheet,
        onDismissRequest = { showSheet = false }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Transaction options",
                style = MaterialTheme.typography.headlineSmall
            )

            Text("Choose an action for this transaction.")

            ManuButton(
                onClick = { showSheet = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Done")
            }
        }
    }
}

@Composable
private fun SnackbarPage() {
    var visible by remember { mutableStateOf(false) }
    var snackbarType by remember { mutableStateOf(ManuSnackbarType.Info) }
    var lastAction by remember { mutableStateOf("No action selected") }

    SectionIntro(
        "Snackbar",
        "Brief feedback messages for actions, with optional action and auto-dismiss."
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ManuButton(onClick = {
            snackbarType = ManuSnackbarType.Info
            visible = true
        }) { Text("Info") }

        ManuButton(onClick = {
            snackbarType = ManuSnackbarType.Success
            visible = true
        }) { Text("Success") }

        ManuButton(onClick = {
            snackbarType = ManuSnackbarType.Error
            visible = true
        }) { Text("Error") }
    }

    Spacer(Modifier.height(8.dp))
    Text(lastAction, style = MaterialTheme.typography.bodySmall)

    ManuSnackbar(
        message = when (snackbarType) {
            ManuSnackbarType.Info -> "This is an informational message."
            ManuSnackbarType.Success -> "Changes saved successfully."
            ManuSnackbarType.Error -> "Something went wrong."
        },
        visible = visible,
        onDismiss = { visible = false },
        type = snackbarType,
        actionLabel = "Undo",
        onAction = {
            lastAction = "Undo action clicked"
            visible = false
        }
    )
}

@Composable
private fun ScaffoldPage() {
    var showFooter by remember { mutableStateOf(true) }

    SectionIntro(
        "ManuScaffold & ManuScreenContainer",
        "A consistent screen structure with optional top and bottom slots and reusable content padding."
    )

    ManuCard(modifier = Modifier.fillMaxWidth()) {
        Text("ManuScaffold preview", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        ManuScaffold(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentPadding = PaddingValues(16.dp),
            topBar = {
                ManuTopBar(title = "Example screen")
            },
            bottomBar = {
                if (showFooter) {
                    Text(
                        "Bottom bar slot",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider()
                }
            }
        ) { contentPadding ->
            ManuScreenContainer(
                modifier = Modifier.padding(contentPadding),
                contentPadding = PaddingValues(0.dp),
                scrollable = true
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Screen content", style = MaterialTheme.typography.titleMedium)
                    Text("This content is inside ManuScreenContainer.")
                    Text("Toggle the bottom slot to preview optional screen regions.")
                    ManuButton(
                        onClick = { showFooter = !showFooter },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showFooter) "Hide bottom bar" else "Show bottom bar")
                    }
                }
            }
        }
    }
}

@Composable
private fun PickerFieldPage() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Personal") }

    SectionIntro(
        "Picker Field",
        "A read-only field that opens a date picker or selection menu managed by your screen."
    )

    ManuPickerField(
        value = selectedDate?.let { java.text.DateFormat.getDateInstance().format(java.util.Date(it)) } ?: "",
        onClick = { showDatePicker = true },
        label = { Text("Transaction date") },
        placeholder = { Text("Choose a date") },
        leadingIcon = Icons.Default.CalendarMonth,
        trailingIcon = Icons.Default.ExpandMore
    )

    ManuDatePicker(
        expanded = showDatePicker,
        onDismissRequest = { showDatePicker = false },
        onDateSelected = {
            selectedDate = it
            showDatePicker = false
        }
    )

    Spacer(Modifier.height(8.dp))

    Box {
        ManuPickerField(
            value = selectedCategory,
            onClick = { showMenu = true },
            label = { Text("Category") },
            leadingIcon = Icons.Default.Receipt,
            trailingIcon = Icons.Default.ExpandMore
        )

        ManuDropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            items = listOf("Personal", "Food", "Transport", "Bills").map { category ->
                ManuMenuItem(
                    label = category,
                    onClick = {
                        selectedCategory = category
                        showMenu = false
                    }
                )
            }
        )
    }
}

@Composable
private fun SectionIntro(title: String, description: String) {
    Text(title, style = MaterialTheme.typography.headlineMedium)
    Text(
        description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
