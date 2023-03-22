package com.boot_board.service;

import com.boot_board.domain.Comment;
import com.boot_board.dto.CommentDTO;
import com.boot_board.dto.PageRequestDTO;
import com.boot_board.dto.PageResponseDTO;
import com.boot_board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService
{
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;


    @Override
    public Long register(CommentDTO commentDTO)
    {
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        return commentRepository.save(comment).getRno();
    }

    @Override
    public CommentDTO read(Long rno)
    {
        Optional<Comment> commentOptional = commentRepository.findById(rno);

        Comment comment = commentOptional.orElseThrow();

        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public void modify(CommentDTO commentDTO)
    {
        Optional<Comment> commentOptional = commentRepository.findById(commentDTO.getRno());

        Comment comment = commentOptional.orElseThrow();

        comment.changeText(commentDTO.getCommentText());

        commentRepository.save(comment);
    }

    @Override
    public void remove(Long rno)
    {
        commentRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<CommentDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO)
    {
        Pageable pageable =
                PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
                        pageRequestDTO.getSize(),
                        Sort.by("rno").ascending());

        Page<Comment> result = commentRepository.listOfBoard(bno, pageable);

        List<CommentDTO> dtoList = result.getContent()
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());


        return PageResponseDTO.<CommentDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
