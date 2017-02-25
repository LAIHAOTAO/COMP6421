package lexical;

import common.CommonMethod;
import common.Const;
import common.Language;
import exception.NoSuchLexemeException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
    
/**
 * Created by ERIC_LAI on 2017-01-17.
 */
public class LexicalScanner {

    private SrcFileReader src;
    private boolean isBackup;
    private Character backupCharacter;
    @SuppressWarnings("Since15")
    private Queue<Character> buffer = new ArrayDeque<Character>();
    private int location;
    private ArrayList<String> keyWordsList;
    private int handlingLocation = 1;

    public LexicalScanner(String path) {
        this.src = new SrcFileReader(path);
        keyWordsList = CommonMethod.getKeyWordsList(Language.KEYWORDS);
    }

    public Token nextToken() {
        Token token = null;
        try {
            token = nextTokenAux();
            while (token != null && token.getType() == TokenType.COMMENT) {
                token = nextTokenAux();
            }
        } catch (NoSuchLexemeException e) {
            e.printStackTrace();
        }
        return token;
    }

    private Token nextTokenAux() throws NoSuchLexemeException {
        StringBuilder tokenBuilder = new StringBuilder();
        Token token = null;
        Character ch = nextChar();
        if (handlingLocation != location) handlingLocation = location;
        // if the first character is a space just ignore it
        while (ch == ' ') {
            ch = nextChar();
        }
        // if the first character is a letter, it should be a keyword or an identifier
        if (Character.isLetter(ch)) {
            tokenBuilder.append(ch);
            ch = nextChar();
            while ((Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')
                    && handlingLocation == location) {
                tokenBuilder.append(ch);
                ch = nextChar();
            }
            // check whether is keyword or not
            String tmp = tokenBuilder.toString();
            if (CommonMethod.isKeyWord(tmp, keyWordsList)) {
                String value = tmp.toUpperCase();
                token = createToken(Enum.valueOf(TokenType.class, value), tmp, handlingLocation);
            } else {
                token = createToken(TokenType.ID, tmp, handlingLocation);
            }
            if (Const.EOF != ch) backupChar();
        }
        // if the first character is a number, there may be two situation:
        // one is that it is an integer, the other is that it is a floating number
        else if (Character.isDigit(ch)) {
            boolean isFloat = false;
            boolean hasDot = false;
            boolean isZero = false;
            if (ch == '0') isZero = true;
            tokenBuilder.append(ch);
            // take the next character
            ch = nextChar();
            if (Character.isDigit(ch) && isZero) {
                backupChar();
                return createToken(TokenType.INT_NUM, "0", handlingLocation);
            }
            while ((Character.isDigit(ch) || ch == '.') && handlingLocation == location) {
                if (Character.isDigit(ch)) {
                    tokenBuilder.append(ch);
                    ch = nextChar();
                }
                // if ch is '.', we need to check the following character
                else {
                    // if the '.' didn't show up before
                    if (!hasDot) {
                        hasDot = true;
                        ch = nextChar();
                        if (!Character.isDigit(ch)) {
                            // if the following character is not a number
                            addDotBack(ch);
                            return createToken(TokenType.INT_NUM, tokenBuilder.toString(),
                                    location);
                        } else {
                            isFloat = true;
                            tokenBuilder.append(".").append(ch);
                            ch = nextChar();
                        }
                    }
                    // if the '.' did show up before
                    else {
                        String str = tokenBuilder.toString();
                        backupChar();
                        return createToken(TokenType.FLOAT_NUM, str, handlingLocation);
                    }
                }
            }
            String num = tokenBuilder.toString();
            if (isFloat) {
                token = createToken(TokenType.FLOAT_NUM, num, handlingLocation);
            } else {
                token = createToken(TokenType.INT_NUM, num, handlingLocation);
            }
            if (Const.EOF != ch) backupChar();
        }
        // if the first character is neither a letter nor a number
        else {
            switch (ch) {
                case '=':
                    ch = nextChar();
                    if (ch == '=') {
                        token = createToken(TokenType.EQ, "==", location);
                    } else {
                        token = createToken(TokenType.ASSIGNMENT, "=", location);
                        backupChar();
                    }
                    break;
                case '>':
                    ch = nextChar();
                    if (ch == '=') {
                        token = createToken(TokenType.GEQ, ">=", location);
                    } else {
                        token = createToken(TokenType.GT, ">", location);
                        backupChar();
                    }
                    break;
                case '<':
                    ch = nextChar();
                    if (ch == '=') {
                        token = createToken(TokenType.LEQ, "<=", location);
                    } else if (ch == '>') {
                        token = createToken(TokenType.NEQ, "<>", location);
                    } else {
                        token = createToken(TokenType.LT, "<", location);
                        backupChar();
                    }
                    break;
                case '/':
                    ch = nextChar();
                    if (ch != '*') {
                        token = createToken(TokenType.DIVISION, "/", location);
                        backupChar();
                    }
                    // handle the multiple-line comments
                    else {
                        // if there '/*' appear means that the following characters in buffer
                        // must be comment, since single line comment has been removed
                        clearBuffer(buffer);
                        int l = location;
                        while (true) {
                            ch = nextChar();
                            if (ch == '*') {
                                ch = nextChar();
                                if (ch == '/') {
                                    return createToken(TokenType.COMMENT,
                                            "multi-line comment",
                                            location);
                                }
                            }
                            if (ch == Const.EOF) {
                                return createToken(TokenType.LEXICAL_ERR,
                                        "unclosed multi-line comment",
                                        l);
                            }
                        }
                    }
                    break;
                case '*':
                    token = createToken(TokenType.MULTIPLICATION, "*", location);
                    break;
                case '+':
                    token = createToken(TokenType.ADDITION, "+", location);
                    break;
                case '-':
                    token = createToken(TokenType.SUBTRACTION, "-", location);
                    break;
                case '(':
                    token = createToken(TokenType.LCP, "(", location);
                    break;
                case ')':
                    token = createToken(TokenType.RCP, ")", location);
                    break;
                case '{':
                    token = createToken(TokenType.LBP, "{", location);
                    break;
                case '}':
                    token = createToken(TokenType.RBP, "}", location);
                    break;
                case '[':
                    token = createToken(TokenType.LSP, "[", location);
                    break;
                case ']':
                    token = createToken(TokenType.RSP, "]", location);
                    break;
                case ';':
                    token = createToken(TokenType.SEMICOLON, ";", location);
                    break;
                case '.':
                    token = createToken(TokenType.DOT, ".", location);
                    break;
                case Const.EOF:
                    break;
                default:
//                    throw new NoSuchLexemeException(String.valueOf(ch), location, src.getFilePath
//                            ());
                    token = createToken(TokenType.LEXICAL_ERR,
                            "Unknown lexeme " + String.valueOf(ch),
                            location);
            }
        }
        return token;
    }

    private void addDotBack(char ch) {
        Queue<Character> tmp = new ArrayDeque<Character>();
        tmp.add('.');
        tmp.add(ch);
        while (!buffer.isEmpty()) {
            tmp.add(buffer.remove());
        }
        buffer = tmp;
    }

    private char nextChar() {
        // if there is not a backup character
        if (!isBackup) {
            if (buffer.isEmpty()) {
                String[] tmp;
                char[] chs;
                do {
                    tmp = src.getLine();
                    // if reach the end of the file
                    if (tmp == null) {
                        return '\uffff';
                    }
                    // if does not reach the end of file
                    chs = tmp[Const.LINE_VALUE].toCharArray();
                } while (chs.length == 0);
                // go through each character add them the buffer
                for (char ch : chs) {
                    buffer.add(ch);
                }
                location = Integer.parseInt(tmp[Const.LINE_NUM]);
            }
            // if buffer is not empty, dequeue the first one
            backupCharacter = buffer.remove();
        }
        // if there is a backup character, just return the backup
        else {
            isBackup = false;
        }
        return backupCharacter;
    }

    private void backupChar() {
        isBackup = true;
    }

    private Token createToken(TokenType type, String value, int location) {
        return new Token(type, value, location);
    }

    private void clearBuffer(Queue queue) {
        while (!queue.isEmpty()) {
            queue.remove();
        }
    }
}
