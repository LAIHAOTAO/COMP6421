	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 4	% set the stack pointer to the top position of the stack
	addi	R14, R0, 65	% 
	sw	-4(R2), R14	% 
	lw	R8, -4(R2)	% 
	ceqi	R8, R8, 1	% 
	bz	R8, else_1	% if statement
	addi	R6, R0, 65	% 
	sw	-4(R2), R6	% 
	j	endif_1	% jump out of the else block
else_1	addi	R9, R0, 66	% 
	sw	-4(R2), R9	% 
endif_1	nop	% end of the if statement
	lw	R11, -4(R2)	% 
	putc	R11	% 
	hlt	% ======end of program======
