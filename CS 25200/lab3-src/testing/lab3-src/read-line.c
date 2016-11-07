#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define MAX_BUFFER_LINE 2048

// Buffer where line is stored
int line_length;
int pos;
char line_buffer[MAX_BUFFER_LINE];

// Simple history array
// This history does not change. 
// Yours have to be updated.
int history_index = 0;
char **history = NULL;
int history_length = 0;
int history_size = 10;

void read_line_print_usage() {
  char * usage = "\n"
    " ctrl-?       Print usage\n"
    " Backspace    Deletes last character\n"
    " up arrow     See last command in the history\n"
    " ctrl-d       Delete key at current position\n"
    " ctrl-a       Move to beginning of line\n"
    " ctrl-e       Move to end of line\n";

  write(1, usage, strlen(usage));

  if (isatty(0)) {
    if (getenv("PROMPT") != NULL) {
      printf("%s", getenv("PROMPT"));
    }
    else {
      printf("myshell>");
    }
    fflush(stdout);
  }
}

/* 
 * Input a line with some basic editing.
 */
char * read_line() {

  // Set terminal in raw mode
  tty_raw_mode();

  line_length = 0;
  pos = 0;

  if(history == NULL)
    history = (char**) malloc(history_size * sizeof(char*));

  // Read one line until enter is typed
  while (1) {

    // Read one character in raw mode.
    char ch;
    read(0, &ch, 1);

//-------------------------------------------------------------------

    if (ch >= 32 && ch != 127) {
      // It is a printable character. 

      write(1,&ch,1);

      // If max number of chars
      if (line_length==MAX_BUFFER_LINE-2) 
        break; 
      
      if(pos != line_length) {
        line_length++;
    
        for(int i = line_length; i > pos; i--)
          line_buffer[i] = line_buffer[i-1];

        line_buffer[pos]=ch;
    
        for(int i = pos + 1; i < line_length; i++) {
          ch = line_buffer[i];
          write(1, &ch, 1);
        }
    
        ch = 8;
        for(int i = pos +1 ; i < line_length; i++)
          write(1, &ch, 1); 
      }
      else {
        // add char to buffer.
        line_buffer[line_length]=ch;
        line_length++;
      }
      
      pos++;
    }

//-------------------------------------------------------------------

    else if(ch == 4  && line_length != 0) {
      // ctrl-d
      for (int i = pos; i < line_length - 1; i++) {
          line_buffer[i] = line_buffer[i+1];
          write(1,line_buffer[i],1);
      }

      line_buffer[line_length] = ' ';
      line_length--;

      for(int i = pos; i < line_length; i++) {
          ch = line_buffer[i];
          write(1, &ch, 1);
      }
      
      ch = ' ';
      write(1, &ch, 1);
      
      ch = 8;
      write(1, &ch, 1);
      
      for(int i = line_length; i > pos; i--)
        write(1, &ch, 1);
    }

//-------------------------------------------------------------------

    else if (ch == 31) {
      // ctrl-?
      read_line_print_usage();
      line_buffer[0]=0;
      break;
    }

//-------------------------------------------------------------------

    else if (ch == 10) {
      // <Enter> was typed. Return line
      
      // Print newline
      write(1,&ch,1);
      
      // Add line to history
      if(history_length == history_size) {
        history_size *=2; 
        history = (char**) realloc(history, history_size*sizeof(char*));
      }

      line_buffer[line_length]=0;

      if(line_buffer[0]) {
        history[history_length] = strdup(line_buffer);
        history_length++;
      }

      break;
    }

//-------------------------------------------------------------------

    else if ((ch == 127 || ch == 8) && line_length != 0) {
      // backspace
      for (int i = pos - 1; i < line_length - 1; i++) {
          line_buffer[i] = line_buffer[i+1];
          write(1,line_buffer[i],1);
      }

      line_buffer[line_length] = ' ';
      line_length--;
      pos--;

      for(int i = pos; i < line_length; i++) {
          ch = line_buffer[i];
          write(1, &ch, 1);
      }

      ch = 8;
      write(1, &ch, 1);
      
      ch = ' ';
      write(1, &ch, 1);
      
      ch = 8;
      write(1, &ch, 1);
      
      for(int i = line_length; i > pos; i--)
        write(1, &ch, 1);
    }

//-------------------------------------------------------------------

    else if(ch == 5) {
      // ctrl-e
      for(int i = pos; i < line_length; i++) {
        char ch = line_buffer[i];
        write(1, &ch, 1);
        pos++;
      }
    }

//-------------------------------------------------------------------

    else if(ch == 1) {
      // Ctrl-a
      for(int i = pos; i > 0; i--) {
        ch = 8;
        write(1,&ch,1);
        pos--;
      }
    }

//-------------------------------------------------------------------

    else if (ch==27) {
      // Escape sequence. Read two chars more
      //
      // HINT: Use the program "keyboard-example" to
      // see the ascii code for the different chars typed.
      //
      char ch1; 
      char ch2;
      read(0, &ch1, 1);
      read(0, &ch2, 1);

      // right
      if (ch1 == 91 && ch2 == 67) {
        if (pos < line_length) {
            char ch = line_buffer[pos];
            write(1, &ch, 1);
            pos++;
          }
      }

      //~~~~~~~~~~~~~~~~~~~~~~~~~~

      // left
      if (ch1 == 91 && ch2 == 68) {
        if (pos > 0) {
            ch = 8;
            write(1,&ch,1);
            pos--;
          }
      }

      //~~~~~~~~~~~~~~~~~~~~~~~~~~~

      // up
      if (ch1==91 && ch2==65 && history_length > 0) {

        // Erase
        for (int i =line_length - pos; i < line_length; i++) {
          ch = 8;
          write(1,&ch,1);
        }

        // Print spaces on top
        for (int i = 0; i < line_length; i++) {
          ch = ' ';
          write(1,&ch,1);
        }

        // Print backspaces
        for (int i = 0; i < line_length; i++) {
          ch = 8;
          write(1,&ch,1);
        }

        // Copy line from history
        strcpy(line_buffer, history[history_index]);
        line_length = strlen(line_buffer);
        history_index=(history_index+1)%history_length;
        
        // echo line
        write(1, line_buffer, line_length);
        pos = line_length;
      }

      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~

      // down
      if (ch1==91 && ch2==66) {
        // Erase
        for (int i = line_length - pos; i < line_length; i++) {
          ch = 8;
          write(1,&ch,1);
        }

        // spaces
        for (int i = 0; i < line_length; i++) {
          ch = ' ';
          write(1,&ch,1);
        }

        // backspaces
        for (int i = 0; i < line_length; i++) {
          ch = 8;
          write(1,&ch,1);
        } 

        if(history_index > 0) {
            strcpy(line_buffer, history[history_index]);
            line_length = strlen(line_buffer);
            history_index=(history_index-1)%history_length;
            
            write(1, line_buffer, line_length);
            pos = line_length;
        }
        else {
            strcpy(line_buffer, "");
            line_length = strlen(line_buffer);

            write(1, line_buffer, line_length);
            pos = line_length;
        }
      } 
    }
  }

  line_buffer[line_length]=10;
  line_length++;
  line_buffer[line_length]=0;

  return line_buffer;
}