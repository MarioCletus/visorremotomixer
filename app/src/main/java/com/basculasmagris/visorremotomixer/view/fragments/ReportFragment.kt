package com.basculasmagris.visorremotomixer.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.basculasmagris.visorremotomixer.BuildConfig
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentReportBinding
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.tableview.TableViewAdapter
import com.basculasmagris.visorremotomixer.tableview.TableViewListener
import com.basculasmagris.visorremotomixer.tableview.TableViewModel
import com.basculasmagris.visorremotomixer.tableview.model.Cell
import com.basculasmagris.visorremotomixer.tableview.model.ColumnHeader
import com.basculasmagris.visorremotomixer.tableview.model.RowHeader
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Constants.FIELD_SEPARATOR
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.filter.Filter
import com.evrencoskun.tableview.pagination.Pagination
import com.evrencoskun.tableview.pagination.Pagination.OnTableViewPageTurnedListener
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.File
import java.io.FileWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToLong


class ReportFragment : Fragment() {

    private lateinit var mBinding: FragmentReportBinding
    private var mLocalDetailRound: MutableList<RoundRunDetail> = ArrayList()
    private var mFilteredLocalDetailRound: MutableList<RoundRunDetail> = ArrayList()


    private val TAG = "REPORT"

    private var moodFilter: Spinner? = null
    private  var genderFilter:Spinner? = null
    private var previousButton: ImageButton? = null
    private var nextButton:ImageButton? = null
    private var tablePaginationDetails: TextView? = null
    private var mTableView: TableView? = null
    private var mTableFilter // This is used for filtering the table.
            : Filter? = null
    private var mPagination // This is used for paginating the table.
            : Pagination? = null
    private val mPaginationEnabled = false
    private var TableViewAdapter: TableViewAdapter? = null
    private var currentType = R.id.rb_opt_carga
    private var calStartDate = Calendar.getInstance()
    private var calEndDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentReportBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    fun calendarToDate(context: Context, calendar: Calendar?, dateFormat: String?): String? {
        if (calendar == null) {
            return null
        }
        val locale: Locale = context.resources.configuration.locale
        val df: DateFormat = SimpleDateFormat(dateFormat, locale)
        val timeZone = TimeZone.getTimeZone("UTC")
        df.timeZone = timeZone
        val d = calendar.time
        return df.format(d)
    }

    override fun onResume() {
        super.onResume()
        mLocalDetailRound = (requireActivity() as MainActivity).mLocalDetailRound
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_report, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.share_report -> {
                        val outputDir = context!!.cacheDir // context being the Activity pointer
                        var outputFile : File? = null
                        var subject = ""
                        var body = ""

                        try {
                            val currentUser = Helper.getCurrentUser(requireActivity())
                            when (currentType) {
                                R.id.rb_opt_carga -> {
                                    Log.i("Report", "rb_opt_carga")
                                    outputFile = File.createTempFile("reporte-carga-${mBinding.tvCalendar.text.toString().replace('/','-').replace("\\s".toRegex(), "")}", ".csv", outputDir)
                                    Log.i("Report", "outputFile carga: $outputFile")

                                    subject = "SPI Mixer - Reporte de carga - ${mBinding.tvCalendar.text}"
                                    body = "Hola,\nEl reporte adjunto fue generado desde la aplicación por el usuario ${currentUser.displayName}"
                                    var fileWriterLoad: FileWriter?
                                    fileWriterLoad = FileWriter(outputFile.absolutePath)
                                    fileWriterLoad.append(Constants.COLUMNS_LOAD_XLS.joinToString(FIELD_SEPARATOR))
                                    fileWriterLoad.append('\n')
                                    mFilteredLocalDetailRound.forEach { roundRunDetail ->
                                        val roundPlaningWeight = roundRunDetail.round.diet.products.sumOf { product -> product.targetWeight }
                                        val roundRealWeight = roundRunDetail.round.diet.products.sumOf { product -> product.finalWeight - product.initialWeight}
                                        roundRunDetail.round.diet.products.forEach { productDetail ->

                                            fileWriterLoad.append(
                                                "${roundRunDetail.round.name}$FIELD_SEPARATOR" +
                                                "${roundRunDetail.userDisplayName}$FIELD_SEPARATOR" +
                                                "${Helper.formattedDate(roundRunDetail.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_XLS_FORMAT_DATE)}$FIELD_SEPARATOR" +
                                                "${Helper.diffDate(roundRunDetail.startDate, roundRunDetail.endDate, Constants.APP_DB_FORMAT_DATE)}$FIELD_SEPARATOR" +
                                                "${roundPlaningWeight.roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${roundRealWeight.roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${(roundRealWeight - roundPlaningWeight).roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${roundRunDetail.round.diet.name}$FIELD_SEPARATOR" +
                                                "${productDetail.name}$FIELD_SEPARATOR" +
                                                "${productDetail.targetWeight.roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${(productDetail.finalWeight-productDetail.initialWeight).roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${(productDetail.finalWeight-productDetail.initialWeight-productDetail.targetWeight).roundToLong()}Kg$FIELD_SEPARATOR")
                                            fileWriterLoad.append('\n')
                                        }
                                    }
                                    fileWriterLoad.close()
                                }

                                R.id.rb_opt_descarga -> {
                                    outputFile = File.createTempFile("reporte-descarga-${mBinding.tvCalendar.text.toString().replace('/','-').replace("\\s".toRegex(), "")}", ".csv", outputDir)
                                    subject = "SPI Mixer - Reporte de descarga - ${mBinding.tvCalendar.text}"
                                    body = "Hola,\nEl reporte adjunto fue generado desde la aplicación por el usuario ${currentUser.displayName}"
                                    var fileWriterDownload: FileWriter? = null
                                    fileWriterDownload = FileWriter(outputFile.absolutePath)
                                    fileWriterDownload.append(Constants.COLUMNS_DOWNLOAD_XLS.joinToString(FIELD_SEPARATOR))
                                    fileWriterDownload.append('\n')

                                    mFilteredLocalDetailRound.forEach { roundRunDetail ->
                                        val roundPlaningWeight = roundRunDetail.round.corrals.sumOf { corral -> corral.customTargetWeight }
                                        val roundRealWeight = roundRunDetail.round.corrals.sumOf { corral -> corral.initialWeight - corral.finalWeight }
                                        roundRunDetail.round.corrals.forEach { corralDetail ->
                                            fileWriterDownload.append(
                                                "${roundRunDetail.round.name}$FIELD_SEPARATOR" +
                                                "${roundRunDetail.userDisplayName}$FIELD_SEPARATOR" +
                                                "${Helper.formattedDate(roundRunDetail.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_XLS_FORMAT_DATE)}$FIELD_SEPARATOR" +
                                                "${Helper.diffDate(roundRunDetail.startDate, roundRunDetail.endDate, Constants.APP_DB_FORMAT_DATE)}$FIELD_SEPARATOR" +
                                                "${roundPlaningWeight.roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${roundRealWeight.roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${(roundRealWeight - roundPlaningWeight).roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${roundRunDetail.round.diet.name}$FIELD_SEPARATOR" +
                                                "${corralDetail.name}$FIELD_SEPARATOR" +
                                                "${(corralDetail.customTargetWeight).roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${(corralDetail.initialWeight-corralDetail.finalWeight).roundToLong()}Kg$FIELD_SEPARATOR" +
                                                "${(corralDetail.initialWeight - corralDetail.finalWeight - corralDetail.customTargetWeight).roundToLong()}Kg$FIELD_SEPARATOR")
                                            fileWriterDownload.append('\n')
                                        }
                                    }

                                    fileWriterDownload.close()
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        Log.i("Report", "outputFile: " +
                                "$outputFile")
                        outputFile?.let {
                            Log.i("Report", "subject: $subject")
                            Log.i("Report", "body: $body")

                            val sendIntent = Intent()
                            val uri = FileProvider.getUriForFile(
                                requireActivity(),
                                BuildConfig.APPLICATION_ID + "." + requireActivity().localClassName + ".provider",
                                it
                            )
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.type = "text/*"
                            sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
                            val putExtra = sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                            sendIntent.putExtra(Intent.EXTRA_TEXT, body)
                            startActivity(Intent.createChooser(sendIntent, "SHARE"))
                        }

                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mLocalDetailRound = (requireActivity() as MainActivity).mLocalDetailRound
        Log.i(TAG, "mLocalDetailRound  ${mLocalDetailRound.size}")

        mLocalDetailRound.forEach {
            Log.i(TAG, "${it.id} | ${it.startDate} | ${it.endDate}")
        }

        mBinding.ivCalendar.setOnClickListener{

        }

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val sdfFull = SimpleDateFormat(Constants.APP_DB_FORMAT_DATE)

        val now = LocalDate.now()
        calStartDate[Calendar.YEAR] = now.year
        calStartDate[Calendar.MONTH] = now.monthValue-1
        calStartDate[Calendar.DAY_OF_MONTH] = Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH)
        calStartDate.set(Calendar.HOUR_OF_DAY, 0)

        calEndDate[Calendar.YEAR] = now.year
        calEndDate[Calendar.MONTH] = now.monthValue-1
        calEndDate[Calendar.DAY_OF_MONTH] = now.dayOfMonth
        calEndDate.set(Calendar.HOUR_OF_DAY, 23)
        calEndDate.set(Calendar.MINUTE, 59)
        calEndDate.set(Calendar.SECOND, 59)

        //mFilteredLocalDetailRound.clear()
        mFilteredLocalDetailRound = mLocalDetailRound.filter {
            Helper.logRoundRunComplete(it)
            sdfFull.parse(it.startDate)!! >= calStartDate.time && sdfFull.parse(it.endDate)!! <= calEndDate.time
        } as MutableList<RoundRunDetail>
        if (mFilteredLocalDetailRound.isNotEmpty()){
            mFilteredLocalDetailRound = mFilteredLocalDetailRound.sortedByDescending { it.startDate } as MutableList<RoundRunDetail>
        }

        Log.i("REPORT", "Datos: : ${mLocalDetailRound.size} | ${mFilteredLocalDetailRound.size}")

        mBinding.tvCalendar.text = "${sdf.format(calStartDate.time)} al ${sdf.format(calEndDate.time)}"

        mBinding.tvCalendar.setOnClickListener {

            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DATE_RANGE_PICKER")

            /*DatePickerDialog(
                requireActivity(),
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()*/

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                val sdf1 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val sdfApp = SimpleDateFormat(Constants.APP_DB_FORMAT_DATE, Locale.getDefault())
                sdf1.timeZone = TimeZone.getTimeZone("UTC")
                val startDate = sdf1.format(it.first)
                val endDate = sdf1.format(it.second)

                //val startDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(sdf.format(Date(it.first)))
                //val endDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(sdf.format(Date(it.second)))
                calStartDate.time = sdf1.parse(startDate) as Date
                calStartDate.add(Calendar.DATE, 1)
                calStartDate.set(Calendar.HOUR_OF_DAY, 0)


                calEndDate.time = sdf1.parse(endDate) as Date
                calEndDate.add(Calendar.DATE, 1)
                calEndDate.set(Calendar.HOUR_OF_DAY, 23)
                calEndDate.set(Calendar.MINUTE, 59)
                calEndDate.set(Calendar.SECOND, 59)

                Log.i("REPORT", "FECHA INICIO: ${calStartDate.time}")
                Log.i("REPORT", "FECHA FIN:  ${calEndDate.time}")

                mBinding.tvCalendar.text = "$startDate al $endDate"
                //mFilteredLocalDetailRound.clear()
                mFilteredLocalDetailRound = mLocalDetailRound.filter {
                    Log.i("REPORT", "FECHA startDate: ${sdfApp.parse(it.startDate)}")
                    sdfApp.parse(it.startDate)!! >= calStartDate.time && sdfApp.parse(it.startDate)!! <= calEndDate.time
                } as MutableList<RoundRunDetail>
                if (mFilteredLocalDetailRound.isNotEmpty()) {
                    mFilteredLocalDetailRound =
                        mFilteredLocalDetailRound.sortedByDescending { it.startDate } as MutableList<RoundRunDetail>
                }

                Log.i(
                    "REPORT",
                    "Datos: : ${mLocalDetailRound.size} | ${mFilteredLocalDetailRound.size}"
                )
                refreshTableTableView(currentType)
            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(
                    requireActivity(),
                    "${datePicker.headerText} is cancelled",
                    Toast.LENGTH_LONG
                ).show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(requireActivity(), "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }

        mBinding.rgType.check(R.id.rb_opt_carga)
        mBinding.rgType.setOnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                // Changes the textview's text to "Checked: example radiobutton text"
                Log.i(TAG, "Checked: ${checkedRadioButton.text}" + checkedRadioButton.text)
                when (checkedRadioButton.id) {
                    R.id.rb_opt_ronda -> {
                        currentType = R.id.rb_opt_ronda
                        Log.i(TAG, "Opción ronda completa")
                        /*mBinding.rvReportList.layoutManager = GridLayoutManager(requireActivity(), 1)
                        val roundRunReportAdapter =  RoundRunReportAdapter(
                            this@ReportFragment,
                            R.id.rb_opt_ronda)
                        roundRunReportAdapter.roundRunList(mLocalDetailRound)
                        mBinding.rvReportList.adapter = roundRunReportAdapter*/
                    }

                    R.id.rb_opt_carga -> {
                        Log.i(TAG, "Opción carga")
                        currentType = R.id.rb_opt_carga
                        refreshTableTableView(R.id.rb_opt_carga)

                        /*mBinding.rvReportList.layoutManager = GridLayoutManager(requireActivity(), 1)
                        val roundRunReportAdapter =  RoundRunReportAdapter(
                            this@ReportFragment,
                            R.id.rb_opt_carga)
                        roundRunReportAdapter.roundRunList(mLocalDetailRound)
                        mBinding.rvReportList.adapter = roundRunReportAdapter*/
                    }

                    R.id.rb_opt_descarga -> {
                        currentType = R.id.rb_opt_descarga
                        Log.i(TAG, "Opción Descarga")
                        Log.i(TAG, "Opción carga")
                        refreshTableTableView(R.id.rb_opt_descarga)

                        /*mBinding.rvReportList.layoutManager = GridLayoutManager(requireActivity(), 1)
                        val roundRunReportAdapter =  RoundRunReportAdapter(
                            this@ReportFragment,
                            R.id.rb_opt_descarga)
                        roundRunReportAdapter.roundRunList(mLocalDetailRound)
                        mBinding.rvReportList.adapter = roundRunReportAdapter*/
                    }
                }
            }
        }


        /*mBinding.rvReportList.layoutManager = GridLayoutManager(requireActivity(), 1)
        val roundRunReportAdapter =  RoundRunReportAdapter(
            this@ReportFragment,
            R.id.rb_opt_carga)
        roundRunReportAdapter.roundRunList(mLocalDetailRound)
        mBinding.rvReportList.adapter = roundRunReportAdapter*/

        val searchField = view.findViewById<EditText>(R.id.query_string)
        searchField.addTextChangedListener(mSearchTextWatcher)

        val itemsPerPage = view.findViewById<Spinner>(R.id.items_per_page_spinner)

        val tableTestContainer = view.findViewById<View>(R.id.table_test_container)

        previousButton = view.findViewById(R.id.previous_button)
        nextButton = view.findViewById(R.id.next_button)
        val pageNumberField = view.findViewById<EditText>(R.id.page_number_text)
        tablePaginationDetails = view.findViewById(R.id.table_details)

        if (mPaginationEnabled) {
            tableTestContainer.visibility = View.VISIBLE
            itemsPerPage.onItemSelectedListener = onItemsPerPageSelectedListener
            previousButton?.setOnClickListener(mClickListener)
            nextButton?.setOnClickListener(mClickListener)
            pageNumberField.addTextChangedListener(onPageTextChanged)
        } else {
            itemsPerPage.onItemSelectedListener = onItemsPerPageSelectedListener
            tableTestContainer.visibility = View.GONE
        }

        // Let's get TableView

        // Let's get TableView
        mTableView = view.findViewById(R.id.tableview)

        initializeTableView()


        mTableView?.let {
            mTableFilter = Filter(it) // Create an instance of a Filter and pass the
            // created TableView.
            if (mPaginationEnabled) {
                // Create an instance for the TableView pagination and pass the created TableView.
                mPagination = Pagination(it)

                // Sets the pagination listener of the TableView pagination to handle
                // pagination actions. See onTableViewPageTurnedListener variable declaration below.
                mPagination?.setOnTableViewPageTurnedListener(onTableViewPageTurnedListener)
            }
        }

        refreshTableTableView(currentType)
    }

    private fun initializeTableView() {
        // Create TableView View model class  to group view models of TableView
        val tableViewModel = TableViewModel()

        // Create TableView Adapter
        val tableViewAdapter = TableViewAdapter(tableViewModel)
        mTableView?.setAdapter<ColumnHeader, RowHeader, Cell>(tableViewAdapter)
        mTableView?.tableViewListener = mTableView?.let { TableViewListener(it) }

        // Create an instance of a Filter and pass the TableView.
        //mTableFilter = new Filter(mTableView);

        // Load the dummy data to the TableView
        tableViewAdapter.setAllItems(
            tableViewModel.randomColumnHeaderList(R.id.rb_opt_carga), tableViewModel
                .simpleRowHeaderList(R.id.rb_opt_carga, mLocalDetailRound), tableViewModel.cellListForSortingTest(R.id.rb_opt_carga, mLocalDetailRound)
        )
    }

    private fun refreshTableTableView(type: Int) {
        // Create TableView View model class  to group view models of TableView
        val tableViewModel = TableViewModel()

        // Create TableView Adapter
        val tableViewAdapter = TableViewAdapter(tableViewModel)
        mTableView?.setAdapter<ColumnHeader, RowHeader, Cell>(tableViewAdapter)
        mTableView?.tableViewListener = mTableView?.let { TableViewListener(it) }

        Log.i(TAG, "mFilteredLocalDetailRound: ${mFilteredLocalDetailRound.size}")
        // Load the dummy data to the TableView
        tableViewAdapter.setAllItems(
            tableViewModel.randomColumnHeaderList(type), tableViewModel
                .simpleRowHeaderList(type, mFilteredLocalDetailRound), tableViewModel.cellListForSortingTest(type, mFilteredLocalDetailRound)
        )
    }

    fun filterTable(filter: String) {
        // Sets a filter to the table, this will filter ALL the columns.
        mTableFilter?.set(filter)
    }

    fun filterTableForMood(filter: String) {
        // Sets a filter to the table, this will only filter a specific column.
        // In the example data, this will filter the mood column.
        //mTableFilter?.set(TableViewModel.MOOD_COLUMN_INDEX, filter)
    }

    fun filterTableForGender(filter: String) {
        // Sets a filter to the table, this will only filter a specific column.
        // In the example data, this will filter the gender column.
        //mTableFilter?.set(TableViewModel.GENDER_COLUMN_INDEX, filter)
    }

    // The following four methods below: nextTablePage(), previousTablePage(),
    // goToTablePage(int page) and setTableItemsPerPage(int itemsPerPage)
    // are for controlling the TableView pagination.
    fun nextTablePage() {
        mPagination?.nextPage()
    }

    fun previousTablePage() {
        mPagination?.previousPage()
    }

    fun goToTablePage(page: Int) {
        mPagination?.goToPage(page)
    }

    fun setTableItemsPerPage(itemsPerPage: Int) {
        if (mPagination != null) {
            mPagination?.itemsPerPage = itemsPerPage
        }
    }

    // Handler for the changing of pages in the paginated TableView.
    private val onTableViewPageTurnedListener =
        OnTableViewPageTurnedListener { _, itemsStart, itemsEnd ->
            val currentPage = mPagination!!.currentPage
            val pageCount = mPagination?.pageCount
            previousButton!!.visibility = View.VISIBLE
            nextButton!!.visibility = View.VISIBLE
            if (currentPage == 1 && pageCount == 1) {
                previousButton?.visibility = View.INVISIBLE
                nextButton!!.visibility = View.INVISIBLE
            }
            if (currentPage == 1) {
                previousButton?.visibility = View.INVISIBLE
            }
            if (currentPage == pageCount) {
                nextButton!!.visibility = View.INVISIBLE
            }
            tablePaginationDetails!!.text = getString(
                R.string.table_pagination_details,
                currentPage.toString(),
                itemsStart.toString(),
                itemsEnd.toString()
            )
        }

    private val mItemSelectionListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // 0. index is for empty item of spinner.
                if (position > 0) {
                    val filter = position.toString()
                    if (parent === moodFilter) {
                        filterTableForMood(filter)
                    } else if (parent === genderFilter) {
                        filterTableForGender(filter)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Left empty intentionally.
            }
        }

    private val mSearchTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            filterTable(s.toString())
            /*val text = s.toString()
            when (currentType) {
                R.id.rb_opt_carga -> {
                    mFilteredLocalDetailRound = mLocalDetailRound.filter {  roundRunDetail ->
                        roundRunDetail.round.name.contains(text) ||
                        roundRunDetail.round.diet.name.contains(text) ||
                        roundRunDetail.round.diet.products.any { productDetail ->
                            (productDetail.name.contains(text))
                        }
                    }.toMutableList()
                }
                R.id.rb_opt_descarga -> {
                    mFilteredLocalDetailRound = mLocalDetailRound.filter {  roundRunDetail ->
                        roundRunDetail.round.name.contains(text) ||
                                roundRunDetail.round.diet.name.contains(text) ||
                                roundRunDetail.round.corrals.any { corralDetail ->
                                    (corralDetail.name.contains(text))
                                }
                    }.toMutableList()
                }
            }

            refreshTableTableView(currentType)*/
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private val onItemsPerPageSelectedListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val itemsPerPage: Int = if ("All" == parent.getItemAtPosition(position).toString()) {
                    0
                } else {
                    parent.getItemAtPosition(position).toString().toInt()
                }
                setTableItemsPerPage(itemsPerPage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    private val mClickListener =
        View.OnClickListener { v ->
            if (v === previousButton) {
                previousTablePage()
            } else if (v === nextButton) {
                nextTablePage()
            }
        }

    private val onPageTextChanged: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val page: Int = if (TextUtils.isEmpty(s)) {
                1
            } else {
                s.toString().toInt()
            }
            goToTablePage(page)
        }

        override fun afterTextChanged(s: Editable) {}
    }
}