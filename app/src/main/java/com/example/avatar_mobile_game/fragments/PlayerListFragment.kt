import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.avatar_mobile_game.DataManager.PlayerRecord
import com.example.avatar_mobile_game.DataManager.RecordsManager
import com.example.avatar_mobile_game.R
import com.example.avatar_mobile_game.adapters.PlayerAdapter
import com.example.avatar_mobile_game.interfaces.PlayerCallback

class PlayerListFragment : Fragment() {

    private lateinit var records_RV_list: RecyclerView
    private lateinit var records_LBL_no_players: TextView
    private var allRecords: List<PlayerRecord> = emptyList()

    var playerClicked: PlayerCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_playerlist, container, false)
        findViews(v)
        initViews()
        return v
    }

    private fun findViews(v: View) {
        records_RV_list = v.findViewById(R.id.records_RV_list)
        records_LBL_no_players = v.findViewById(R.id.records_LBL_no_players)
        records_RV_list.layoutManager = LinearLayoutManager(context)
    }

    private fun initViews() {
        allRecords = RecordsManager.getInstance().getRecords()
        val adapter = PlayerAdapter(allRecords).apply {
            onPlayerClickListener = playerClicked
        }
        records_RV_list.adapter = adapter
        updateUI()
    }

    private fun updateUI() {
        if (allRecords.isEmpty()) {
            records_LBL_no_players.visibility = View.VISIBLE
            records_RV_list.visibility = View.GONE
        } else {
            records_LBL_no_players.visibility = View.GONE
            records_RV_list.visibility = View.VISIBLE
        }
    }

//    private lateinit var records_RV_list: RecyclerView
//    private lateinit var records_LBL_no_players: TextView
//    private var allRecords: List<PlayerRecord> = emptyList()
//
//    var playerClicked: PlayerCallback? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val v = inflater.inflate(R.layout.fragment_playerlist, container, false)
//        findViews(v)
//        initViews()
//        return v
//    }
//
//    private fun findViews(v: View) {
//        records_LBL_no_players = v.findViewById(R.id.records_LBL_no_players)
//        records_RV_list = v.findViewById(R.id.records_RV_list)
//        records_RV_list.layoutManager = LinearLayoutManager(context)
//    }
//
//    private fun initViews() {
//        // Fetch records
//        allRecords = RecordsManager.getInstance().getRecords()
//
//        // Initialize adapter and assign the callback
//        val adapter = PlayerAdapter(allRecords)
//        adapter.onPlayerClickListener = playerClicked
//        records_RV_list.adapter = adapter
//
//        // Update UI based on the records list
//        updateUI()
//    }
//
//    private fun updateUI() {
//        if (allRecords.isEmpty()) {
//            records_LBL_no_players.visibility = View.VISIBLE
//            records_RV_list.visibility = View.GONE
//        } else {
//            records_LBL_no_players.visibility = View.GONE
//            records_RV_list.visibility = View.VISIBLE
//        }
//    }
}
