/*
 *  The scanner definition for COOL.
 */

/*
 *  Stuff enclosed in %{ %} in the first section is copied verbatim to the
 *  output, so headers and global definitions are placed here to be visible
 * to the code in the file.  Don't remove anything that was here initially
 */
%{
#include <cool-parse.h>
#include <stringtab.h>
#include <utilities.h>

/* The compiler assumes these identifiers. */
#define yylval cool_yylval
#define yylex  cool_yylex

/* Max size of string constants */
#define MAX_STR_CONST 1025
#define YY_NO_UNPUT   /* keep g++ happy */

extern FILE *fin; /* we read from this file */

/* define YY_INPUT so we read from the FILE fin:
 * This change makes it possible to use this scanner in
 * the Cool compiler.
 */
#undef YY_INPUT
#define YY_INPUT(buf,result,max_size) \
	if ( (result = fread( (char*)buf, sizeof(char), max_size, fin)) < 0) \
		YY_FATAL_ERROR( "read() in flex scanner failed");

char string_buf[MAX_STR_CONST]; /* to assemble string constants */
char *string_buf_ptr;

extern int curr_lineno;
extern int verbose_flag;
extern YYSTYPE cool_yylval;
int cnt = 0;
/*
 *  Add Your own definitions here
 */

char strBuf[MAX_STR_CONST];
int comm = 0;

int appendstr(char* p) {
    if (strlen(strBuf) + strlen(p) >= MAX_STR_CONST) return 0;
    strcat(strBuf, p);
    return 1;
}


void addstr() {
    cool_yylval.symbol = stringtable.add_string(strBuf);
   // printf("find:%s\n", strBuf);
    strBuf[0] = 0;
}


void addid(char* s) {
    cool_yylval.symbol = idtable.add_string(s);
}


void addint(char* s) {
    cool_yylval.symbol = inttable.add_string(yytext);
}

%}

/*
 * Define names for regular expressions here.
 */

DARROW          =>
LE              <=
ASSIGN          <-

a               [aA]
b               [bB]
c               [cC]
d               [dD]
e               [eE]
f               [fF]
g               [gG]
h               [hH]
i               [iI]
j               [jJ]
k               [kK]
l               [lL]
m               [mM]
n               [nN]
o               [oO]
p               [pP]
q               [qQ]
r               [rR]
s               [sS]
t               [tT]
u               [uU]
v               [vV]
w               [wW]
x               [xX]
y               [yY]
z               [zZ]

D               [0-9]
W               [a-zA-Z]
SPACE           [ \x20\r\t\v\f]
VALID           [\<\~\.\{\}\:\;\(\)\,\+\-\*\=\/\@\~]
INVALID         [\_\!\#\$\%\^\`\|\\\[\]\?/&\>]
%x              STR
%x              ESC
%x              COM
%x              STR_ERR

%%

{SPACE}*                            { ;                 }


 /*
  *  Nested comments
  */

"--"[^\n]*                          { ;                 }
<COM,INITIAL>"(*"                   { 
                                        BEGIN (COM);
                                        comm++;
                                    }
<COM,INITIAL>"*)"                   {
                                        comm--;
                                        if (comm<0) {
                                            cool_yylval.error_msg = "Unmatched *)";
                                            comm = 0;
                                            return (ERROR);
                                        }
                                        if (comm == 0) BEGIN (INITIAL);
                                    }
<COM>[^\*\(\)\n]*                   { ;                 }
<COM>\*                             { ;                 }
<COM>\(                             { ;                 }
<COM>\)                             { ;                 }
<COM>\n+                            { curr_lineno += yyleng;    }
<COM><<EOF>>                        {
                                        BEGIN (STR_ERR);
                                        cool_yylval.error_msg = "EOF in string comment";
                                        return (ERROR);
                                    }

 /*
  *  The multiple-character operators.
  */
{DARROW}                            { return (DARROW);  }
{LE}                                { return (LE);      }
{ASSIGN}                            { return (ASSIGN);  }
{VALID}                             { return *(yytext); }
 /*
  * Keywords are case-insensitive except for the values true and false,
  * which must begin with a lower-case letter.
  */
\n+                                 { curr_lineno += yyleng;}
{INVALID}                           {
                                        cool_yylval.error_msg = yytext;
                                        return (ERROR);
                                    }
{c}{l}{a}{s}{s}                     { return (CLASS);   }
{e}{l}{s}{e}                        { return (ELSE);    }
{f}{i}                              { return (FI);      }
{i}{f}                              { return (IF);      }
{i}{n}                              { return (IN);      }
{i}{n}{h}{e}{r}{i}{t}{s}            { return (INHERITS);}
{i}{s}{v}{o}{i}{d}                  { return (ISVOID);  }
{l}{e}{t}                           { return (LET);     }
{l}{o}{o}{p}                        { return (LOOP);    }
{p}{o}{o}{l}                        { return (POOL);    }
{t}{h}{e}{n}                        { return (THEN);    }
{w}{h}{i}{l}{e}                     { return (WHILE);   }
{c}{a}{s}{e}                        { return (CASE);    }
{e}{s}{a}{c}                        { return (ESAC);    }
{o}{f}                              { return (OF);      }
{n}{e}{w}                           { return (NEW);     }
{n}{o}{t}                           { return (NOT);     }
 /* BOOL_CONST */
f{a}{l}{s}{e}                       { cool_yylval.boolean = 0; return (BOOL_CONST);}
t{r}{u}{e}                          { cool_yylval.boolean = 1; return (BOOL_CONST);}

[A-Z]({D}|{W}|_)*                   {
                                        addid(yytext);
                                        return (TYPEID);  
                                    }
[a-z]({D}|{W}|_)*                   { 
                                        addid(yytext);
                                        return (OBJECTID);
                                    }
 /* INT_CONST */
{D}+                                { 
                                        addint(yytext);
                                        return (INT_CONST);
                                    }

 /*
  *  String constants (C syntax)
  *  Escape sequence \c is accepted for all characters c. Except for 
  *  \n \t \b \f, the result is c.
  *
  */
\"                                  BEGIN(STR);
<STR>\\                             BEGIN(ESC);
<ESC>b                              {
                                        if (!appendstr("\b")) {
                                            cool_yylval.error_msg = "String constant too long";
                                            BEGIN (STR_ERR);
                                            return (ERROR);
                                        } else BEGIN (STR);
                                    }
<ESC>t                              {
                                        if (!appendstr("\t")) {
                                            cool_yylval.error_msg = "String constant too long";
                                            BEGIN (STR_ERR);
                                            return (ERROR);
                                        } else BEGIN(STR);
                                
                                    }
<ESC>n                              {
                                        if (!appendstr("\n")) {
                                            cool_yylval.error_msg = "String constant too long";
                                            BEGIN (STR_ERR);
                                            return (ERROR);
                                        } else {BEGIN(STR);}
                                    }
<ESC>f                              {
                                        if (!appendstr("\f")) {
                                            cool_yylval.error_msg = "String constant too long";
                                            BEGIN (STR_ERR);
                                            return (ERROR);
                                        } else BEGIN(STR);
                                    }
<ESC>\0                             {
                                        cool_yylval.error_msg = "String contains escaped null character.";
                                        BEGIN (STR_ERR);
                                        return (ERROR);
                                    }
<ESC>\n                             { 
                                        curr_lineno++; 
                                        if (!appendstr("\n")) {
                                            cool_yylval.error_msg = "String constant too long";
                                            BEGIN (STR_ERR);
                                            return (ERROR);
                                        } else BEGIN (STR);
                                    }
<ESC>.                              {
                                        if (!appendstr(yytext)) {
                                            cool_yylval.error_msg = "String constant too long";        
                                            BEGIN (STR_ERR);
                                            return (ERROR);
                                        } else BEGIN(STR);
                                    }
<STR>[^\\\"\0\n]*                   {
                                        if (!appendstr(yytext)) {
                                            cool_yylval.error_msg = "String constant too long";
                                            BEGIN (STR_ERR);
                                            return (ERROR);
                                        }
                                    }
<STR,ESC><<EOF>>                    {
                                        BEGIN (STR_ERR);
                                        cool_yylval.error_msg = "EOF in string constant";
                                        return (ERROR);
                                    }
<STR>\n                             {
                                        BEGIN (INITIAL);
                                        curr_lineno++; 
                                        strBuf[0] = 0;
                                        cool_yylval.error_msg = "Unterminated string constant";
                                        return (ERROR);
                                    }
<STR>\"                             {
                                        BEGIN (INITIAL);
                                        addstr();
                                        return (STR_CONST);
                                    }
<STR,ESC,COM>\0                     {
                                        BEGIN (STR_ERR);
                                        cool_yylval.error_msg = "String contains null character.";
                                        return (ERROR);
                                    }
<STR_ERR>[^\n\"]                    { ;                         }
<STR_ERR>\n                         {
                                        curr_lineno++;
                                        BEGIN (INITIAL);
                                    }
<STR_ERR>\"                         {
                                        BEGIN (INITIAL);
                                        strBuf[0] = 0;
                                    }
[^{SPACE}{VALID}{D}{W}\n\f\t\b\v]   {   
                                        cool_yylval.error_msg = yytext;
                                        return (ERROR);
                                    }
%%
