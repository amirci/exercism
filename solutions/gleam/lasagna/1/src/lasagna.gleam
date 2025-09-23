// Please define the expected_minutes_in_oven function
pub fn expected_minutes_in_oven() {
  40
}

// Please define the remaining_minutes_in_oven function
pub fn remaining_minutes_in_oven(actual) {
  expected_minutes_in_oven() - actual
}

// Please define the preparation_time_in_minutes function
pub fn preparation_time_in_minutes(layers) {
  layers * 2
}

// Please define the total_time_in_minutes function
pub fn total_time_in_minutes(layers, actual) {
   preparation_time_in_minutes(layers) + actual
}

// Please define the alarm function
pub fn alarm() {
  "Ding!"
}
