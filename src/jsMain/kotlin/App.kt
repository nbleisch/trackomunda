import react.*
import react.dom.*
import kotlinext.js.*
import kotlinx.html.js.*
import kotlinx.coroutines.*
import trackomunda.model.GangPayload

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    val (shoppingList, setShoppingList) = useState(emptyList<GangPayload>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setShoppingList(getShoppingList())
        }
    }

    h1 {
        +"Full-Stack Shopping List"
    }
    ul {
        shoppingList.sortedByDescending(GangPayload::alignment).forEach { item ->
            li {
                key = item.toString()
                attrs.onClickFunction = {
                    scope.launch {
                        deleteShoppingListItem(item)
                        setShoppingList(getShoppingList())
                    }
                }
                +"[${item.alignment}] ${item.alignment} "
            }
        }
    }
    child(
        InputComponent,
        props = jsObject {
            onSubmit = { input ->
                //val cartItem = Gang(input.replace("!", ""), input.count { it == '!' })
                scope.launch {
                  //  addShoppingListItem(cartItem)
                  //  setShoppingList(getShoppingList())
                }
            }
        }
    )
}