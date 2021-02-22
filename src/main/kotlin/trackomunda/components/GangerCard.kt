package trackomunda.components

import clients.fetchGang
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

class GangerCard(val ganger: Ganger) : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        h3 { +ganger.name }
    }
}