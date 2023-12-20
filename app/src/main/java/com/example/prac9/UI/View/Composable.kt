package com.example.kotlinpract1.UI.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.kotlinpract1.R
import kotlinx.coroutines.launch

data class Student(val name: String, val group: String)

enum class Screen {
    Home,
    Menu
}

private val textStyle =
    TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )

@Composable
fun DrawerItem(
    screen: Screen,
    currentScreen: Screen,
    onClick: (Screen) -> Unit
) {
    Text(
        text = when (screen) {
            Screen.Home -> "Главная"
            Screen.Menu -> "Меню"
        },
        style = textStyle,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(16.dp)
            .background(if (screen == currentScreen) Color.Gray else Color.Transparent)
            .clickable {
                if (screen != currentScreen) {
                    onClick(screen)
                }
            }
    )
}

@Composable
fun DrawerContent(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Column {
        DrawerItem(
            screen = Screen.Home,
            currentScreen = currentScreen,
            onClick = onScreenSelected
        )
        DrawerItem(
            screen = Screen.Menu,
            currentScreen = currentScreen,
            onClick = onScreenSelected
        )
    }
}

@Composable
fun StudentInfo(studentName: String, group: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ФИО: $studentName\nГруппа: $group",
            style = textStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun StudentListScreen(students: List<Student>, navigateToDetail: (Student) -> Unit) {
    LazyColumn {
        items(students) { student ->
            Text(
                text = "ФИО: ${student.name}\nГруппа: ${student.group}",
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { navigateToDetail(student) }
            )
        }
    }
}

@Composable
fun StudentDetailScreen(student: Student) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ФИО: ${student.name}\nГруппа: ${student.group}",
            style = textStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun MyApp(students: List<Student>) {
    // Инициализация NavController для управления навигацией
    val navController = rememberNavController()
    // Инициализация состояния Scaffold для управления интерфейсом
    val scaffoldState = rememberScaffoldState()
    // Инициализация CoroutineScope для управления корутинами
    val scope = rememberCoroutineScope()
    // Использование mutableStateOf для отслеживания текущего экрана
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    // Создадим список предметов для блока "Пересдачи"
    val retakeSubjects = listOf(
        "Дискретная математика",
        "Сети",
        "История",
        "Информатика"
    )

    // Создание интерфейса приложения с использованием Scaffold
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            // Верхняя панель с названием приложения
            TopAppBar(
                title = {
                    Text(text = "Список студентов")
                }
            )
        },
        bottomBar = {
            // Нижняя панель с кнопкой "Меню"
            BottomAppBar(
                backgroundColor = Color.Blue
            ) {
                IconButton(
                    onClick = {
                        // Открытие бокового меню при нажатии кнопки "Меню"
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Меню")
                }
            }
        },
        drawerContent = {
            // Боковое меню с переключателем экранов
            DrawerContent(
                currentScreen = currentScreen,
                onScreenSelected = { newScreen ->
                    currentScreen = newScreen
                    // Закрытие бокового меню при выборе нового экрана
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    ) { innerPadding ->
        // Навигация между экранами с использованием NavHost
        NavHost(
            navController = navController,
            startDestination = "studentList"
        ) {
            composable("studentList") {
                // Экран списка студентов
                LazyColumn {
                    items(students) { student ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    // Навигация к экрану детальной информации о студенте при выборе студента
                                    navController.navigate("studentDetail/${student.name}/${student.group}")
                                },
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                // Отображение информации о студенте
                                Text(
                                    text = "ФИО: ${student.name}",
                                    style = textStyle,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Группа: ${student.group}",
                                    style = textStyle,
                                    textAlign = TextAlign.Center
                                )
                                // Добавим блок "Пересдачи" с предметами
                                RetakeBlock(retakeSubjects)
                            }
                        }
                    }
                }
            }
            composable(
                "studentDetail/{studentName}/{group}",
                arguments = listOf(
                    navArgument("studentName") { type = NavType.StringType },
                    navArgument("group") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                // Получение параметров из URL
                val studentName = backStackEntry.arguments?.getString("studentName")
                val group = backStackEntry.arguments?.getString("group")
                if (studentName != null && group != null) {
                    // Поиск информации о студенте по его имени и группе
                    val student = students.find { it.name == studentName && it.group == group }
                    if (student != null) {
                        // Экран детальной информации о студенте
                        StudentDetailScreen(student)
                    }
                }
            }
        }
    }
}

@Composable
fun RetakeBlock(subjects: List<String>) {
    // Блок "Пересдачи" с предметами
    Column(
        modifier = Modifier
            .background(Color.Red)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        // Заголовок блока "Пересдачи"
        Text(
            text = "Пересдачи",
            style = textStyle.copy(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        // Отображение списка предметов для пересдачи
        subjects.forEach { subject ->
            Text(
                text = "Предмет: $subject",
                style = textStyle.copy(
                    color = Color.White,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
