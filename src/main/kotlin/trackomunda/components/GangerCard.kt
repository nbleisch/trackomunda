package trackomunda.components

import clients.fetchGang
import com.ccfraser.muirwik.components.table.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.*
import react.dom.*
import kotlinext.js.*
import kotlinx.html.js.*
import kotlinx.coroutines.*
import trackomunda.model.GangPayload
import trackomunda.model.Ganger

class GangerCard : RComponent<GangerProps, RState>() {

    val gangerAttributes = listOf(Ganger::m, Ganger::ws, Ganger::bs, Ganger::s, Ganger::t, Ganger::w, Ganger::i, Ganger::a, Ganger::ld, Ganger::cl, Ganger::wil, Ganger::int, Ganger::xp)

    override fun RBuilder.render() {
        h3 { +props.ganger.name }
        mTable {
            mTableHead {
                mTableRow {
                    gangerAttributes.forEach {
                        mTableCell { +it.name.toUpperCase() }
                    }
                }
            }
            mTableBody {
                mTableRow {
                    gangerAttributes.forEach {
                        mTableCell { +"${it.get(props.ganger)}" }
                    }
                }
            }
        }
    }
}

external interface GangerProps : RProps {
    var ganger: Ganger
}

fun RBuilder.gangerCard(handler: GangerProps.() -> Unit): ReactElement {
    return child(GangerCard::class) {
        this.attrs(handler)
    }
}
