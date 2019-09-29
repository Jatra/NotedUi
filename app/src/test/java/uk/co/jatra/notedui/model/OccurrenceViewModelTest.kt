package uk.co.jatra.notedui.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class OccurrenceViewModelTest {
    @Test
    fun shouldConvert31Dec() {
        val date = LocalDate.of(2019, 12, 31)
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

        assertThat(dateFormatter.format(date)).isEqualTo("31 December 2019")
    }

    @Test
    fun shouldConvert1Jan() {
        val date = LocalDate.of(2020, 1, 1)
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

        assertThat(dateFormatter.format(date)).isEqualTo("01 January 2020")
    }
}