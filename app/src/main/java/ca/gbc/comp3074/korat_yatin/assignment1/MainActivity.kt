
package ca.gbc.comp3074.korat_yatin.assignment1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ca.gbc.comp3074.korat_yatin.assignment1.ui.theme.Assignment1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Assignment1Theme {
                var hours by remember { mutableStateOf("") }
                var rate by remember { mutableStateOf("") }
                var taxRate by remember { mutableStateOf("") }

                var calculated by remember { mutableStateOf(false) }
                var error by remember { mutableStateOf("") }

                var pay by remember { mutableDoubleStateOf(0.0) }
                var overtimePay by remember { mutableDoubleStateOf(0.0) }
                var totalPay by remember { mutableDoubleStateOf(0.0) }
                var tax by remember { mutableDoubleStateOf(0.0) }

                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Pay Calculator",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        OutlinedTextField(
                            value = hours,
                            onValueChange = {
                                hours = it
                                calculated = false
                            },
                            label = { Text("Hours Worked") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = rate,
                            onValueChange = {
                                rate = it
                                calculated = false
                            },
                            label = { Text("Hourly Rate") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = taxRate,
                            onValueChange = {
                                taxRate = it
                                calculated = false
                            },
                            label = { Text("Tax Rate (%)") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                val h = hours.toDoubleOrNull()
                                val r = rate.toDoubleOrNull()
                                val t = taxRate.toDoubleOrNull()

                                if (h == null || r == null || t == null ||
                                    !h.isFinite() || !r.isFinite() ||
                                    !t.isFinite() || h < 0 || r < 0 ||
                                    t < 0 || t > 100
                                ) {
                                    error = "Enter valid values. Tax rate must be 0–100."
                                    calculated = false
                                } else {
                                    pay = minOf(h, 40.0) * r
                                    overtimePay = maxOf(h - 40.0, 0.0) * r * 1.5
                                    totalPay = pay + overtimePay
                                    tax = pay * (t / 100.0)
                                    error = ""
                                    calculated = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Calculate")
                        }

                        Button(
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        AboutActivity::class.java
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("About")
                        }

                        if (error.isNotEmpty()) {
                            Text(
                                error,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (calculated) {
                            HorizontalDivider()
                            Text("Pay: %.2f".format(pay))
                            Text("Overtime Pay: %.2f".format(overtimePay))
                            Text("Total Pay: %.2f".format(totalPay))
                            Text("Tax: %.2f".format(tax))
                        }
                    }
                }
            }
        }
    }
}
