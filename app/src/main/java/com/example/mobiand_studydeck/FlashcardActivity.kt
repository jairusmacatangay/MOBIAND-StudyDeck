package com.example.mobiand_studydeck

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiand_studydeck.databinding.ActivityFlashcardBinding
import com.example.mobiand_studydeck.flashcards.DatabaseHandler
import com.example.mobiand_studydeck.flashcards.FlashcardsModelClass
import com.example.mobiand_studydeck.flashcards.ItemAdapter

class FlashcardActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityFlashcardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener { view ->
            addRecord()
        }

        setupListofDataIntoRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemStudy -> {
                val intent = Intent(this, StudyActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.itemQuiz -> {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                val intent = Intent(this, WordJumbleActivity::class.java)
                startActivity(intent)
                return true
            }
        }
    }

    private fun addRecord() {
        val term = binding.etTerm.text.toString()
        val definition = binding.etDefinition.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!term.isEmpty() && !definition.isEmpty()) {
            val status =
                    databaseHandler.addFlashcard(FlashcardsModelClass(0, term, definition))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                binding.etTerm.text.clear()
                binding.etDefinition.text.clear()
            }
        } else {
            Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
            ).show()
        }
        setupListofDataIntoRecyclerView()
    }

    /**
     * Function is used to get the Items List which is added in the database table.
     */
    private fun getItemsList(): ArrayList<FlashcardsModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val cardList: ArrayList<FlashcardsModelClass> = databaseHandler.viewFlashcard()

        return cardList
    }

    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {

            binding.rvList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE

            binding.rvList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = ItemAdapter(this, getItemsList())
            binding.rvList.adapter = itemAdapter
        } else {

            binding.rvList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Method is used to show the custom update dialog.
     */
    fun updateRecordDialog(flashcardsModelClass: FlashcardsModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.findViewById<EditText>(R.id.etUpdateTerm).setText(flashcardsModelClass.term)
        updateDialog.findViewById<EditText>(R.id.etUpdateDefinition).setText(flashcardsModelClass.definition)

        updateDialog.findViewById<TextView>(R.id.tvUpdate).setOnClickListener(View.OnClickListener {

            val term = updateDialog.findViewById<EditText>(R.id.etUpdateTerm).text.toString()
            val definition = updateDialog.findViewById<EditText>(R.id.etUpdateDefinition).text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!term.isEmpty() && !definition.isEmpty()) {
                val status =
                    databaseHandler.updateFlashcard(FlashcardsModelClass(flashcardsModelClass.id, term, definition))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.findViewById<TextView>(R.id.tvCancel).setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    /**
     * Method is used to show the delete alert dialog.
     */
    fun deleteRecordAlertDialog(flashcardsModelClass: FlashcardsModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete ${flashcardsModelClass.term}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteFlashcard(FlashcardsModelClass(flashcardsModelClass.id, "", ""))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}