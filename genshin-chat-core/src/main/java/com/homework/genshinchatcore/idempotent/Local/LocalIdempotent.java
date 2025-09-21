package com.homework.genshinchatcore.idempotent.Local;

import com.homework.genshinchatcore.idempotent.Idempotent;
import org.springframework.stereotype.Component;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
@Component
public class LocalIdempotent implements Idempotent {

    @Override
    public boolean acquireIdempotentLock(Object id) {
        return true;
    }
}
