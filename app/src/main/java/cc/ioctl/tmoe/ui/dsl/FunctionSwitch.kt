package cc.ioctl.tmoe.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.ioctl.tmoe.hook.base.DynamicHook
import cc.ioctl.tmoe.ui.LocaleController
import cc.ioctl.tmoe.ui.wrapper.cell.TextCheckCell

class FunctionSwitch(
    val hook: DynamicHook,
    val titleKey: String,
    val titleResId: Int,
    val descKey: String? = null,
    val descResId: Int? = null,
    val onClick: View.OnClickListener? = null
) : DslTMsgListItemInflatable, TMsgListItem {

    class ViewHolder(cell: TextCheckCell) : RecyclerView.ViewHolder(cell)

    override val isEnabled = true

    override fun createViewHolder(context: Context, parent: ViewGroup) =
        ViewHolder(TextCheckCell(context))

    override fun bindView(viewHolder: RecyclerView.ViewHolder, position: Int, context: Context) {
        val cell = viewHolder.itemView as TextCheckCell
        val titleString = LocaleController.getString(titleKey, titleResId)
        val descString: String? = descKey?.let {
            LocaleController.getString(it, descResId ?: 0)
        }
        if (descString != null) {
            cell.setTextAndValueAndCheck(titleString, descString, hook.isEnabledByUser, true, true)
        } else {
            cell.setTextAndCheck(titleString, hook.isEnabledByUser, true)
        }
        cell.drawLine = onClick != null
    }

    override fun onItemClick(v: View, position: Int, x: Float, y: Float) {
        val cell = v as TextCheckCell
        if (onClick == null || cell.isInSwitchRange(x.toInt())) {
            val checked = cell.toggle()
            hook.isEnabledByUser = checked
            if (checked) {
                if (!hook.isInitialized && !hook.isPreparationRequired) {
                    hook.initialize()
                }
            }
        } else {
            onClick.onClick(v)
        }
    }
}
