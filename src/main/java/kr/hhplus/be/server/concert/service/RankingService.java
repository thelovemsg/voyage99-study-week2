package kr.hhplus.be.server.concert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.redis.RedisCacheTemplate;
import kr.hhplus.be.server.common.redis.RedisKeyUtils;
import kr.hhplus.be.server.concert.controller.dto.RankingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RedisCacheTemplate cacheTemplate;
    private final ObjectMapper objectMapper;

    public void addToRanking(Long concertScheduleId, String concertTitle, LocalDateTime soldOutTime) throws JsonProcessingException {
        String rankingKey = RedisKeyUtils.getDailyRankingKey();

        RankingInfo rankingInfo = RankingInfo.builder()
                .concertScheduleId(concertScheduleId)
                .concertTitle(concertTitle)
                .soldOutTime(soldOutTime)
                .build();

        String rankingInfoJson = objectMapper.writeValueAsString(rankingInfo);
        double score = calculateScore(soldOutTime);

        cacheTemplate.addToSortedSetForRanking(rankingKey, rankingInfoJson, score);
    }

    /**
     * Redis의 경우 sorted set의 경우 점수가 기본적으로 오름차순으로 저장됨.
     * calculateScore의 경우 크면 클수록 빠르기 때문에, ranking 을 찾아올 때 조심해야함.
     */
    public List<RankingInfo> getTodayRanking(int limit) throws JsonProcessingException {
        String rankingKey = RedisKeyUtils.getDailyRankingKey();

        // 점수 높은 순으로 조회 (빠른 매진 순)
        Set<String> rankingJsons = cacheTemplate.getReverseRangeFromSortedSet(rankingKey, 0, limit - 1);

        List<RankingInfo> rankings = new ArrayList<>();
        int rank = 1;

        for (String json : rankingJsons) {
            RankingInfo info = objectMapper.readValue(json, RankingInfo.class);
            info.setRank(rank++); // 등수 설정
            rankings.add(info);
        }

        return rankings;
    }

    private double calculateScore(LocalDateTime soldOutTime) {
        // 자정부터 매진 시점까지 경과 시간 계산
        LocalDateTime midnight = soldOutTime.toLocalDate().atStartOfDay();
        long secondsFromMidnight = Duration.between(midnight, soldOutTime).getSeconds();

        // 빠를수록 높은 점수 (86400초 = 24시간)
        return 86400.0 - secondsFromMidnight;
    }
}
