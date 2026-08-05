package nep.timeline.freezerUI.ui.modules

import nep.timeline.freezer.binders.ComponentType
import nep.timeline.freezer.binders.UIComponentData
import nep.timeline.freezer.provide.DataBinder
import nep.timeline.freezer.provide.UIServiceBinder

object ModuleRegister {
    fun fetchModules(packageName: String): Map<String, Map<String, LinkedHashSet<IModule>>> {
        val resultMap = LinkedHashMap<String, MutableMap<String, LinkedHashSet<IModule>>>()

        val binder = UIServiceBinder.instance ?: return resultMap
        val scripts: List<String> = binder.scripts.list ?: return resultMap

        for (script in scripts) {
            val pages: List<String> = binder.getPages(script, packageName).list ?: continue
            val pageMap = LinkedHashMap<String, LinkedHashSet<IModule>>()

            for (page in pages) {
                val componentDatas: List<UIComponentData> = binder.getComponents(script, packageName, page).list ?: continue
                if (componentDatas.isEmpty()) continue

                val moduleSet = LinkedHashSet<IModule>()

                for (data in componentDatas) {
                    val module: IModule = when (data.type) {
                        ComponentType.TEXT -> TextModule(
                            title = data.title,
                            onClick = {
                                DataBinder.getInstance()
                                    .get("SCRIPT_FUNCTION\n${script}\n${page}\n${data.uuid}\n${it}\n${packageName}")
                            }
                        )
                        ComponentType.SWITCH -> SwitchModule(
                            title = data.title,
                            summary = data.summary,
                            default = data.boolValue,
                            onChange = { newValue ->
                                DataBinder.getInstance()
                                    .get("SCRIPT_FUNCTION\n${script}\n${page}\n${data.uuid}\n${newValue}\n${packageName}")
                            }
                        )
                        ComponentType.DROPDOWN -> DropdownModule(
                            title = data.title,
                            summary = data.summary,
                            items = data.items ?: emptyList(),
                            default = data.intValue,
                            onChange = { newIndex ->
                                DataBinder.getInstance()
                                    .get("SCRIPT_FUNCTION\n${script}\n${page}\n${data.uuid}\n${newIndex}\n${packageName}")
                            }
                        )
                        ComponentType.SLIDER -> SliderModule(
                            title = data.title,
                            min = data.floatMinValue,
                            max = data.floatMaxValue,
                            increment = data.floatIncrementValue,
                            default = data.floatDefaultValue,
                            onChange = { newValue ->
                                DataBinder.getInstance()
                                    .get("SCRIPT_FUNCTION\n${script}\n${page}\n${data.uuid}\n${newValue}\n${packageName}")
                            }
                        )
                    }
                    moduleSet.add(module)
                }

                if (moduleSet.isNotEmpty()) {
                    pageMap[page] = moduleSet
                }
            }

            if (pageMap.isNotEmpty()) {
                resultMap[script] = pageMap
            }
        }

        return resultMap
    }
}