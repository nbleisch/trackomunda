package trackomunda.components

import clients.fetchGang
import com.ccfraser.muirwik.components.list.MListItemAlignItems
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import kotlinx.coroutines.*
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.px
import kotlinx.css.width
import react.*
import react.dom.h3
import styled.StyleSheet
import styled.css
import trackomunda.model.GangPayload

external interface GangState : RState {
    var gangPayload: GangPayload?
}

class GangRosterSection : RComponent<RProps, GangState>() {

    private val scope = MainScope()

    override fun componentDidMount() {
        scope.launch {
            val newState = fetchGang()
            setState {
                gangPayload = newState?.gang
            }
        }
    }


    override fun RBuilder.render() {

        val themeStyles = object : StyleSheet("ComponentStyles", isStatic = true) {
            val list by css {
                width = 320.px
                backgroundColor = Color("#FFFFFF")
            }
        }
        state.gangPayload?.run {
            h3 {
                +"$gang_name"
            }

            gangers.sortedBy { it.isReady }.forEach {
                gangerCard {
                    ganger = it
                }
            }
        } ?: h3 { +"Gang konnte nicht geladen werden" }
    }
}

fun RBuilder.gangRoster() = child(GangRosterSection::class) {}