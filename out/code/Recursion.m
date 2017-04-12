f1496724653	sw	4(R2), R3	% store return address
	lw	R13, 8(R2)	% 
	ceqi	R13, R13, 0	% 
	bz	R13, else_1	% if statement
	addi	R3, R0, 1	% return value is a static value, get its value
	j	endif_1	% jump out of the else block
else_1	lw	R7, 8(R2)	% 
	subi	R7, R7, 1	% 
	sw	-4(R1), R7	% add parameter
	sw	-12(R1), R2	% store the previous frame pointer
	addi	R2, R1, -12	% update the frame pointer
	addi	R1, R2, -4	% update the stack pointer
	jl	R3, f1496724653	% store return address and jump to f1496724653
	lw	R15, 8(R2)	% 
	mul	R15, R3, R15	% 
	sw	-4(R2), R15	% 
	lw	R5, -4(R2)	% 
	add	R3, R0, R5	% return value is a register value, get its value
endif_1	nop	% end of the if statement
	lw	R8, 4(R2)	% get return address
	lw	R2, 0(R2)	% load the previous frame pointer address
	addi	R1, R1, 16	% reset the stack pointer
	jr	R8	% 
	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 4	% set the stack pointer to the top position of the stack
	addi	R13, R0, 4	% 
	sw	-4(R1), R13	% add parameter
	sw	-12(R1), R2	% store the previous frame pointer
	addi	R2, R1, -12	% update the frame pointer
	addi	R1, R2, -4	% update the stack pointer
	jl	R3, f1496724653	% store return address and jump to f1496724653
	sw	-4(R2), R3	% 
	lw	R7, -4(R2)	% 
	addi	R7, R7, 41	% 
	putc	R7	% 
	hlt	% ======end of program======
