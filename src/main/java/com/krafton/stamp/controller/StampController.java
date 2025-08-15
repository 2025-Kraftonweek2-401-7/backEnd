package com.krafton.stamp.controller;

import com.krafton.stamp.dto.StampResponseDto;
import com.krafton.stamp.dto.UserStampResponseDto;
import com.krafton.stamp.service.StampService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stamps")
@RequiredArgsConstructor
@Tag(name = "Stamps", description = "도감 API")
public class StampController {

    private final StampService stampService;

    @GetMapping
    @Operation(summary = "전체 우표 목록", description = "도감용 전체 우표 목록 반환")
    public ResponseEntity<List<StampResponseDto>> getAll() {
        var list = stampService.getAllStamps()
                .stream().map(StampResponseDto::new).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{stampId}")
    @Operation(summary = "우표 상세", description = "특정 우표 상세 정보")
    public ResponseEntity<StampResponseDto> detail(@PathVariable Long stampId) {
        return ResponseEntity.ok(new StampResponseDto(stampService.getStampDetail(stampId)));
    }



    @DeleteMapping("/{stampId}")
    @Operation(summary = "우표 삭제", description = "특정 우표를 도감에서 삭제합니다.")
    public ResponseEntity<Void> deleteStamp(@PathVariable Long stampId) {
        stampService.deleteStamp(stampId);
        return ResponseEntity.noContent().build();
    }

}
