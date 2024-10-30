import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tahirdev.kyberchat.R

data class SettingItem(val name: String, val icon: Int)

class SettingsAdapter(
    private val settingList: List<SettingItem>,
    private val onItemClick: (SettingItem) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.ItemViewHolder>() {

    // No need for VIEW_TYPE_DIVIDER or getItemViewType since there are no dividers

    override fun getItemCount(): Int {
        return settingList.size // Only return the number of setting items
    }

    // Create view holders for setting items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile, parent, false)
        return ItemViewHolder(view)
    }

    // Bind view holders: bind setting items to the view
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val settingItem = settingList[position]
        holder.bind(settingItem)

        // Set tint color based on item name or icon
        if (settingItem.name == "Delete Account") {
            holder.settingIcon.setColorFilter(
                ContextCompat.getColor(holder.itemView.context, R.color.red), // Set delete icon to red
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            holder.settingIcon.setColorFilter(
                ContextCompat.getColor(holder.itemView.context, R.color.black), // Set other icons to black
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    // ViewHolder for setting items
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val settingName: TextView = view.findViewById(R.id.settingName)
        val settingIcon: ImageView = view.findViewById(R.id.settingIcon)

        fun bind(settingItem: SettingItem) {
            settingName.text = settingItem.name
            settingIcon.setImageResource(settingItem.icon)

            itemView.setOnClickListener {
                onItemClick(settingItem)
            }
        }
    }
}
